package loader

import scala.util.Try
import akka.actor.ActorSystem
import java.io.{ FileOutputStream, PrintWriter }
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Source
import feature._
import spray.json._
import geojson.FeatureJsonProtocol._
import org.elasticsearch.node.NodeBuilder.nodeBuilder
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.client.Client

object Loader extends App {

  val node = nodeBuilder().client(true).node()
  val client = node.client()

  implicit val system = ActorSystem("system")
  import system.dispatcher

  implicit val mat = FlowMaterializer()

  //val addressFile = io.Source.fromFile("src/main/resources/address.geojson", "utf-8")

  //val json = addressFile.getLines.mkString.parseJson
  //val fc = json.convertTo[FeatureCollection]

  //val output = new PrintWriter(new FileOutputStream("src/main/resources/address_load.json"), true)

  //Source(() => fc.features.toIterator).
  Source(() => (1 to 100).toIterator).
    foreach { f =>
      println(f)
      //output.println(FeatureFormat.write(f))
    }.
    onComplete { _ =>
      Try {
        //addressFile.close()
        //output.close()
      }
      loadJson()
      Try(client.close())
      system.shutdown()
    }

  def loadJson() {
    val bulkJson = io.Source.fromFile("src/main/resources/address_load.json").getLines().toList
    val bulkRequest = client.prepareBulk()
    for (i <- 0 to bulkJson.size - 1) {
      bulkRequest.add(client.prepareIndex("address", "point", i.toString).setSource(bulkJson(i)))
    }
    bulkRequest.execute().actionGet()
    println("Bulk load request sent")
  }

  def createIndexWithProperties(indexName: String) = {
    val indexRequest = new CreateIndexRequest(indexName)
    indexRequest.settings(ImmutableSettings.settingsBuilder().
      put("index.number_of_shards", 13).put("index.number_of_replicas", 1).build())
    client.admin().indices().create(indexRequest).actionGet()
  }
}
