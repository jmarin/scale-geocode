package core

import scala.util.{ Success, Failure }
import akka.actor.{ Props, Actor, ActorLogging }
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import model.AddressJsonProtocol._

object AddressSearch {
  def props(host: String, port: Int) =
    Props(new AddressSearch(host, port))
  case class Query(index: String, collection: String, queryString: String)
}

class AddressSearch(host: String, port: Int) extends Actor with ActorLogging {
  import AddressSearch._

  val client = ElasticClient.remote(host, port)

  def receive: Receive = {
    case Query(index, collection, queryString) =>

      println(queryString)

      implicit val ec = context.dispatcher

      val q = search in s"${index}/${collection}" query matchPhrase("ADDRESS", queryString) limit 5

      //val q = search in "address" -> "point" query "OK"

      val f = client.execute {
        q
      }

      val origSender = sender()

      f.onComplete {
        case Success(r) => {
          origSender ! r.toString
        }
        case Failure(_) => origSender ! "Search Failed"
      }

  }

}
