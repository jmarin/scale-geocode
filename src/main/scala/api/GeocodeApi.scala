package api

import scala.util.{ Success, Failure }
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
import core.AddressQuery._

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
      path("address" / "line") {
        post {
          respondWithMediaType(`application/json`) {
            compressResponseIfRequested() {
              import model.AddressJsonProtocol._
              import core.AddressQuery.LineQuery
              entity(as[String]) { json =>
                val inputAddress = json.parseJson.convertTo[AddressInput]
                onComplete((addressSearch ? LineQuery("address", "line", inputAddress.address)).mapTo[Feature]) {
                  case Success(f) => complete(f.toJson.toString)
                  case Failure(_) => complete("Failure")
                }
              }
            }
          }
        }
      } ~
      path("address" / "point") {
        parameter('queryString.as[String], 'maxFeatures.as[Int] ? 1) { (term, maxFeatures) =>
          get {
            respondWithMediaType(`application/json`) {
              compressResponseIfRequested() {
                complete {
                  (addressSearch ? PointQuery("address", "point", term, maxFeatures)).collect {
                    case s: String => s
                    case _ => "Failure"
                  }
                }
              }
            }
          }
        }
      } ~
      pathPrefix("api") {
        path("geocode") {
          import model.AddressJsonProtocol._
          import core.AddressQuery._
          post {
            entity(as[String]) { data =>
              val inputAddresse = data.parseJson.convertTo[AddressInput]
              complete {
                "geocode"
              }
            }
          }
        }
      }

  }

}
