package actors

import org.specs2.mutable._
import akka.actor.Props
import scala.concurrent.duration._
import geometry._
import AddressInterpolator._
import akka.testkit.TestActorRef
import spray.json._
import feature._
import geojson.FeatureJsonProtocol._

class AddressInterpolatorSpec extends Specification {
  sequential

  val p1 = Point(-77, 39)
  val p2 = Point(-76, 40)
  val p3 = Point(-75, 38)
  val p4 = Point(-77, 39)
  val p5 = Point(-78, 39.1)
  val line = Line(Array(p1, p2, p3))

  val fjson = """{ "type": "Feature", "properties": { "TLID": 76225010, "TFIDL": 210419286, "TFIDR": 210415483, "ARIDL": "400331412920", "ARIDR": "400331428599", "LINEARID": "110431686609", "FULLNAME": "M St NW", "LFROMHN": "3100", "LTOHN": "3198", "RFROMHN": "3101", "RTOHN": "3199", "ZIPL": "20007", "ZIPR": "20007", "EDGE_MTFCC": "S1400", "ROAD_MTFCC": "S1400", "PARITYL": "E", "PARITYR": "O", "PLUS4L": null, "PLUS4R": null, "LFROMTYP": null, "LTOTYP": null, "RFROMTYP": null, "RTOTYP": null, "OFFSETL": "N", "OFFSETR": "N" }, "geometry": { "type": "LineString", "coordinates": [ [ -77.061184, 38.90519 ], [ -77.061468, 38.90519 ], [ -77.061704, 38.905185 ], [ -77.061965, 38.905186 ], [ -77.062709, 38.905177 ], [ -77.062811, 38.905177 ] ] } }"""

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

    "choose the correct address range" in new SpecsTestKit {
      val feature = fjson.parseJson.convertTo[Feature]
      val an = 3126
      val range = AddressInterpolator.calculateAddressRange(feature, an)
      range.start % 2 must be equalTo (0)
      range.end % 2 must be equalTo (0)
      range.start must be equalTo (3100)
      range.end must be equalTo (3198)
    }

    "interpolate position on a GeoJSON line segment" in new SpecsTestKit {
      val p = Point(-77.06161564892814, 38.90518687184474)
      val interpolator = system.actorOf(Props[AddressInterpolator])
      val an = 3126
      interpolator ! InterpolateGeoJson(fjson, an)
      expectMsgType[Point] must be equalTo (p)
    }
  }

}
