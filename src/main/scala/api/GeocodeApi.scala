package api

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.ask
import akka.util.Timeout
import feature.{ Feature, FeatureCollection }
import geojson.FeatureJsonProtocol.FeatureFormat
import model.{ TotalHits, Hits, AddressSearchResult, AddressInput, AddressOutput }
import scala.concurrent.duration._
import spray.routing.{ Directives, Route }
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json._
import spray.http.MediaTypes._
import spray.http.BodyPart
import spray.http.MultipartFormData

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
      path("address" / "point") {
        post {
          respondWithMediaType(`application/json`) {
            import model.AddressJsonProtocol._
            entity(as[String]) { data =>
              val inputAddresses = data.parseJson.convertTo[List[AddressInput]]
              addressSearch ! inputAddresses
              complete {
                (addressSearch ? inputAddresses).collect {
                  case features: List[Feature] =>
                    import geojson.FeatureJsonProtocol._
                    println(features)
                    FeatureCollectionFormat.write(FeatureCollection(features)).toString
                  case s: String => s
                  case _ => "Failure"
                }
              }
            }
          }
        }
      } ~
      path("address" / "point" / "suggest") {
        import core.AddressSearch._
        parameter('queryString.as[String]) { term =>
          get {
            respondWithMediaType(`application/json`) {
              compressResponseIfRequested() {
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

  }

}
