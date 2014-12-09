package main

import akka.actor.{ Props, ActorSystem }
import api._
import spray.can.Http
import akka.io.IO
import core._

object ScaleGeocode extends GeocodeApi {

  def main(args: Array[String]): Unit = {
    println("ScaleGeocode started")
    implicit val system = ActorSystem("scale-geocode")

    val addressSearch = system.actorOf(AddressSearch.props("192.168.59.103", 9200))

    val geocodeService = system.actorOf(
      GeocodeService.props(geocodeRoute(addressSearch)))

    IO(Http)(system) ! Http.Bind(geocodeService, "0.0.0.0", port = 8080)

  }

}
