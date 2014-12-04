package actors

import org.specs2.mutable._
import akka.actor.Props
import scala.concurrent.duration._
import geometry._
import LineInterpolator._

class LineInterpolatorSpec extends Specification {
  sequential

  val p1 = Point(-77, 39)
  val p2 = Point(-76, 40)
  val p3 = Point(-75, 38)
  val p4 = Point(-77, 39)
  val p5 = Point(-78, 39.1)
  val line = Line(Array(p1, p2, p3))

  "LineInterpolator" should {
    "extract points at certain distances" in new SpecsTestKit {
      val p1 = Point(-76.57573593128807, 39.42426406871193)
      val p2 = Point(-75.26832815729998, 38.53665631459995)

      within(2 seconds) {
        val interpolator = system.actorOf(Props[LineInterpolator])
        interpolator ! Interpolate(line, 0.6)
        expectMsgType[Point] must be equalTo (p1)
        interpolator ! Interpolate(line, -0.6)
        expectMsgType[Point] must be equalTo (p2)
      }

    }

  }

}
