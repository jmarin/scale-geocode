package actors

import akka.actor.{ Props, Actor, ActorLogging }
import geometry._

object LineInterpolator {
  case class Interpolate(line: Line, d: Double)
}

class LineInterpolator extends Actor with ActorLogging {
  import LineInterpolator._

  def receive: Receive = {
    case Interpolate(line, d) =>
      sender() ! line.pointAtDist(d)
  }

}
