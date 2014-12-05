package actors

import org.specs2.mutable._
import akka.actor.Props
import scala.concurrent.duration._
import geometry._
import AddressInterpolator._

class AddressInterpolatorSpec extends Specification {
  sequential

  val p1 = Point(-77, 39)
  val p2 = Point(-76, 40)
  val p3 = Point(-75, 38)
  val p4 = Point(-77, 39)
  val p5 = Point(-78, 39.1)
  val line = Line(Array(p1, p2, p3))

  "AddressInterpolator" should {
    "extract points at certain distances" in new SpecsTestKit {
      val p = Point(-75.61217082451263, 39.224341649025256)

      within(2 seconds) {
        val interpolator = system.actorOf(Props[AddressInterpolator])
        val ar = AddressRange(100, 300)
        val an = 225
        interpolator ! Interpolate(line, ar, an)
        expectMsgType[Point] must be equalTo (p)
      }

    }

  }

}
