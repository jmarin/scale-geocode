package api

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.ask
import akka.util.Timeout
import feature.Feature
import geojson.FeatureJsonProtocol.FeatureFormat
import model.{ TotalHits, Hits, AddressSearchResult }
import scala.concurrent.duration._
import spray.routing.{ Directives, Route }
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json._

trait GeocodeApi extends Directives {

  implicit val timeout = Timeout(10 seconds)

  def geocodeRoute(addressSearch: ActorRef)(implicit system: ActorSystem): Route = {
    path("") {
      getFromResource("web/index.html")
    } ~ {
      getFromResourceDirectory("web")
    } ~
      path("test") {
        get {
          complete {
            "OK"
          }
        }
      } ~
      path("address" / "point" / "suggest") {
        import core.AddressSearch._
        parameter('queryString.as[String]) { term =>
          get {
            complete {
              (addressSearch ? Query("address", "point", term)).collect {
                case s: String => s
                case _ => "Failure"
              }
            }
          }
        }
      }

  }

}
