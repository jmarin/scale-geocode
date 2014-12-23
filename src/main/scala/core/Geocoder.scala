package core

import geometry._
import feature._
import model._

object Geocoder {

  def geocode(address: AddressInput): Feature = {
    val point = Point(-77, 38)
    val feature = Feature(point)
    feature
  }
}

