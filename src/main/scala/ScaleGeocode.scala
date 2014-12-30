package main

import akka.actor.{ Props, ActorSystem }
import api._
import spray.can.Http
import akka.io.IO
import core._
import com.typesafe.config.ConfigFactory
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

object ScaleGeocode extends GeocodeApi {

  def main(args: Array[String]): Unit = {
    println("ScaleGeocode started")
    implicit val system = ActorSystem("scale-geocode")
    val config = ConfigFactory.load()
    val host = config.getString("elasticsearch.host")
    val port = config.getInt("elasticsearch.port")
    val client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port))

    val addressSearch = system.actorOf(AddressSearch.props(client))

    val geocodeService = system.actorOf(
      GeocodeService.props(geocodeRoute(addressSearch)))

    IO(Http)(system) ! Http.Bind(geocodeService, "0.0.0.0", port = 8080)

  }

}
