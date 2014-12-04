package actors

import akka.actor.{ Props, Actor, ActorLogging }
import geometry._

object LineInterpolator {
  case class Interpolate(line: Line, d: Double)
  sealed trait AddressRange
  case class LeftAddressRange(start: Int, end: Int) extends AddressRange
  case class RightAddressRange(start: Int, end: Int) extends AddressRange
}

class LineInterpolator extends Actor with ActorLogging {
  import LineInterpolator._

  def splitAddressRange(l1: Int, l2: Int, d: Double): Double = {

  }

  def receive: Receive = {
    case Interpolate(line, d) =>
      sender() ! line.pointAtDist(d)
  }

}
