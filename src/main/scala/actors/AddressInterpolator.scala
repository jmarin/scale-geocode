package actors

import akka.actor.{ Props, Actor, ActorLogging }
import geometry._
import feature._
import spray.json._
import geojson.FeatureJsonProtocol._

object AddressInterpolator {
  case class AddressRange(start: Int, end: Int)
  case class Interpolate(line: Line, ar: AddressRange, an: Int)
  case class InterpolateGeoJson(fjson: String, an: Int)

  // Naive and incomplete implementation, for demonstration purposes only
  def calculateAddressRange(f: Feature, a: Int): AddressRange = {
    val addressIsEven = (a % 2 == 0)
    val rightRangeIsEven = (f.values.getOrElse("RFROMHN", "0").toString.toInt % 2 == 0)
    val leftRangeIsEven = (f.values.getOrElse("LFROMHN", "0").toString.toInt % 2 == 0)

    val prefix =
      if (addressIsEven && rightRangeIsEven)
        "R"
      else if (addressIsEven && leftRangeIsEven)
        "L"
      else if (!addressIsEven && !rightRangeIsEven)
        "R"
      else if (!addressIsEven && !leftRangeIsEven)
        "L"

    val end = f.values.getOrElse(s"${prefix}TOHN", "0").toString.toInt
    val start = f.values.getOrElse(s"${prefix}FROMHN", "0").toString.toInt

    AddressRange(start, end)
  }

  def interpolate(line: Line, range: AddressRange, a: Int): Double = {
    val l = line.length
    val d = range.end - range.start
    val x = a - range.start
    val dist = x * l / d
    dist
  }

}

class AddressInterpolator extends Actor with ActorLogging {
  import AddressInterpolator._

  def receive: Receive = {
    case Interpolate(l, ar, an) =>
      val d = interpolate(l, ar, an)
      sender() ! l.pointAtDist(d)
    case InterpolateGeoJson(fjson, an) =>
      val f = fjson.parseJson.convertTo[Feature]
      val ar = calculateAddressRange(f, an)
      val l = f.geometry.asInstanceOf[Line]
      val d = interpolate(l, ar, an)
      sender() ! l.pointAtDist(d)
  }

}
