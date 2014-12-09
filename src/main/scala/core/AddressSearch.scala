package core

import scala.util.{ Success, Failure }
import akka.actor.{ Props, Actor, ActorLogging }
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

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

      implicit val ec = context.dispatcher

      val f = client.execute {
        search in s"${index}/${collection}" query queryString
      }
      f.onComplete {
        case Success(r) => sender() ! r
        case Failure(_) => sender() ! "Search Failed"
      }

  }

}
