package core

import geometry._
import feature._
import geojson.FeatureJsonProtocol.{ FeatureFormat, FeatureCollectionFormat }
import model.{ AddressSearchResult, AddressInput, AddressOutput }
import scala.util.{ Success, Failure }
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{ Props, Actor, ActorLogging }
import org.elasticsearch.client.Client
import model.AddressJsonProtocol._
import parser.AddressParser
import spray.json._
import DefaultJsonProtocol._
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.index.query.FilterBuilders
import org.elasticsearch.index.query.FilterBuilders._
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.QueryBuilders._
import scala.collection.JavaConversions._
import geojson.FeatureJsonProtocol._

object AddressQuery {
  def props(client: Client) =
    Props(new AddressQuery(client)(Timeout(1 seconds)))
  case class PointQuery(index: String, collection: String, address: String, maxFeatures: Int)
  case class LineQuery(index: String, collection: String, address: String)
  case class Geocode(index: String, collection: String, address: String)
}

class AddressQuery(client: Client)(implicit val timeout: Timeout) extends Actor with ActorLogging {
  import AddressQuery._

  def receive: Receive = {
    case Geocode(index, collection, address) =>
      val fp = self ? PointQuery(index, collection, address, 1)
      val fl = self ? LineQuery(index, collection, address)
      for {
        p <- fp
        l <- fl
      } yield (fp, fl)

    case PointQuery(index, collection, queryString, maxFeatures) =>
      val features = searchPoint(client, index, collection, queryString).take(maxFeatures)
      val geoJson = features.map { f =>
        f.toJson.toString
      }
      if (features.size > 1) {
        sender() ! "[" + geoJson.mkString(",") + "]"
      } else {
        sender() ! geoJson
      }

    case LineQuery(index, collection, queryString) =>
      val address = AddressParser(queryString)
      val number = address.number
      val street = address.street
      val zipCode = address.zipCode
      val state = address.state

      val rightHouseFilter = FilterBuilders.andFilter(
        FilterBuilders.rangeFilter("RFROMHN").lte(number),
        FilterBuilders.rangeFilter("RTOHN").gte(number))

      val leftHouseFilter = FilterBuilders.andFilter(
        FilterBuilders.rangeFilter("LFROMHN").lte(number),
        FilterBuilders.rangeFilter("LTOHN").gte(number))

      val houseFilter = FilterBuilders.orFilter(rightHouseFilter, leftHouseFilter)

      val zipLeftFilter = FilterBuilders.termFilter("ZIPL", zipCode)
      val zipRightFilter = FilterBuilders.termFilter("ZIPR", zipCode)
      val zipFilter = FilterBuilders.orFilter(zipLeftFilter, zipRightFilter)
      val filter = FilterBuilders.andFilter(houseFilter, zipFilter)

      val stateQuery = QueryBuilders.matchQuery("STATE", state)
      val streetQuery = QueryBuilders.matchPhraseQuery("FULLNAME", street)
      val boolQuery = QueryBuilders
        .boolQuery()
        .must(stateQuery)
        .must(streetQuery)

      val q = QueryBuilders.filteredQuery(boolQuery, filter)

      val response = client.prepareSearch(index)
        .setTypes(collection)
        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        .setQuery(q)
        .execute
        .actionGet

      val hits = response.getHits().getHits
      val feature = FeatureFormat.read(hits(0).getSourceAsString.parseJson)
      val addressRange = AddressInterpolator.calculateAddressRange(feature, number)
      val point = AddressInterpolator.interpolate(feature, addressRange, number)
      sender() ! point

    case inputAddresses: List[AddressInput] =>
      val origSender = sender()
      val point = Point(-77, 38)
      val f = Feature(point)
      val output = inputAddresses.map { input =>
        val id = Field("id", IntType())
        val address = Field("address", StringType())
        val schema = Schema(id, address)
        val values = Map("geometry" -> point, "id" -> input.id, "address" -> input.address)
        //val geoJson = (origSender ? Query("address", "point", input.address)).await
        //val fc = FeatureCollectionFormat.read(geoJson)
        //val f = fc.features.head
        f
      }

      origSender ! output

  }

  def searchPoint(client: Client, index: String, collection: String, queryString: String): Array[Feature] = {
    val response = client.prepareSearch(index)
      .setTypes(collection)
      .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
      .setQuery(QueryBuilders.matchPhraseQuery("ADDRESS", queryString))
      .execute
      .actionGet

    val hits = response.getHits().getHits
    println(hits)
    hits.map(hit => hit.getSourceAsString.parseJson.convertTo[Feature])
  }

}
