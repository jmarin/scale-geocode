package main

import akka.actor.{ Props, ActorSystem }

object ScaleGeocode {

  def main(args: Array[String]): Unit = {
    println("ScaleGeocode started")
    val system = ActorSystem("scale-geocode")
  }

}
