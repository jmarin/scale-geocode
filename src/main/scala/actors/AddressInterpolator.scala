package actors

import akka.actor.{ Props, Actor, ActorLogging }
import geometry._

object AddressInterpolator {
  case class AddressRange(start: Int, end: Int)
  case class Interpolate(line: Line, ar: AddressRange, an: Int)
}

class AddressInterpolator extends Actor with ActorLogging {
  import AddressInterpolator._

  def interpolate(line: Line, range: AddressRange, a: Int): Double = {
    val l = line.length
    val d = range.end - range.start
    val x = a - range.start
    val dist = x * l / d
    dist
  }

  def receive: Receive = {
    case Interpolate(l, ar, an) =>
      val d = interpolate(l, ar, an)
      sender() ! l.pointAtDist(d)
  }

}
