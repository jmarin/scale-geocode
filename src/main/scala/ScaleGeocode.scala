package main

import akka.actor.{ Props, ActorSystem }
import api._
import spray.can.Http
import akka.io.IO
import core._
import com.typesafe.config.ConfigFactory

object ScaleGeocode extends GeocodeApi {

  def main(args: Array[String]): Unit = {
    println("ScaleGeocode started")
    implicit val system = ActorSystem("scale-geocode")
    val config = ConfigFactory.load()
    val host = config.getString("elasticsearch.host")
    val port = config.getInt("elasticsearch.port")

    val addressSearch = system.actorOf(AddressSearch.props(host, port))

    val geocodeService = system.actorOf(
      GeocodeService.props(geocodeRoute(addressSearch)))

    IO(Http)(system) ! Http.Bind(geocodeService, "0.0.0.0", port = 8080)

  }

}
