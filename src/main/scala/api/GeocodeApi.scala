package api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import spray.routing.{ Directives, Route }
import scala.concurrent.ExecutionContext.Implicits.global

trait GeocodeApi extends Directives {

  implicit val timeout = Timeout(10 seconds)

  def geocodeRoute(addressSearch: ActorRef): Route =
    path("test") {
      get {
        complete {
          "OK"
        }
      }
    } ~
      path("geocode" / "point" / "suggest") {
        import core.AddressSearch._
        parameter('queryString.as[String]) { queryString =>
          get {
            complete {
              (addressSearch ? Query("address", "point", queryString)).collect {
                case s: String => s
                case _ => "Failure"
              }
            }
          }
        }
      }

}
