package api

import akka.actor.Props
import spray.routing.{ HttpServiceActor, Route }

object GeocodeService {
  def props(route: Route): Props =
    Props(new GeocodeService(route))
}

class GeocodeService(route: Route) extends HttpServiceActor {

  def receive: Receive = runRoute(route)

}
