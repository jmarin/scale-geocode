package core

import geometry._
import feature._
import geojson.FeatureJsonProtocol.{ FeatureFormat, FeatureCollectionFormat }
import model.{ AddressSearchResult, AddressInput, AddressOutput }
import scala.util.{ Success, Failure }
import akka.pattern.ask
import akka.util.Timeout
import akka.actor.{ Props, Actor, ActorLogging }
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import model.AddressJsonProtocol._
import spray.json._
import DefaultJsonProtocol._

object AddressSearch {
  def props(host: String, port: Int) =
    Props(new AddressSearch(host, port))
  case class Query(index: String, collection: String, queryString: String)
  case class LineQuery(index: String, collection: String, number: Int, address: String, zipCode: String, state: String)
}

class AddressSearch(host: String, port: Int) extends Actor with ActorLogging {
  import AddressSearch._

  val interpolator = context.actorOf(Props[AddressInterpolator], "interpolator")

  val client = ElasticClient.remote(host, port)

  def receive: Receive = {
    case Query(index, collection, queryString) =>

      implicit val ec = context.dispatcher

      val q = search in s"${index}/${collection}" query matchPhrase("ADDRESS", queryString) limit 5

      val f = client.execute {
        q
      }

      val origSender = sender()

      f.onComplete {
        case Success(r) => {
          val jsonAst = r.toString.parseJson
          val results = jsonAst.convertTo[AddressSearchResult]
          val hits = results.hits
          val features = hits.hits.map(hit => hit._source)
          origSender ! "[" + features.map(f => FeatureFormat.write(f)).mkString(",") + "]"
        }
        case Failure(_) => origSender ! "Search Failed"
      }

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

    case address: AddressInput =>
      sender() ! Geocoder.geocode(address)
  }

}
