package model

import org.specs2.mutable.Specification
import spray.json._

class AddressJsonProtocolSpec extends Specification {
  import model.AddressJsonProtocol._

  "Address Results" should {
    "Serialize hits" in {
      val hit1Json =
        """{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.50523515,35.82827709,0.0]},"properties":{"ADDRESS":"18997 Doss Rd Evansville 72729 OK","ID":4,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_ynH2ldRc_qGNxDJg","_index":"address"}"""
      val hit2Json =
        """{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.49891782,35.79143163,0.0]},"properties":{"ADDRESS":"22543 Oklahoma Evansville 72729 OK","ID":6,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_ypUildRc_qGNxDJi","_index":"address"}"""

      val hit1 = hit1Json.parseJson.convertTo[Hits]
      //println(hit1.toString)
      hit1.toJson.toString must be equalTo (hit1Json)
      val hit2 = hit2Json.parseJson.convertTo[Hits]
      hit2.toJson.toString must be equalTo (hit2Json)
    }
    "serialize total hits" in {
      val totalHitsJson =
        """{"total":20,"max_score":1.0,"hits":[{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.50523515,35.82827709,0.0]},"properties":{"ADDRESS":"18997DossRdEvansville72729OK","ID":4,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_ynH2ldRc_qGNxDJg","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.49891782,35.79143163,0.0]},"properties":{"ADDRESS":"22543OklahomaEvansville72729OK","ID":6,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_ypUildRc_qGNxDJi","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.55673368,36.12684084,0.0]},"properties":{"ADDRESS":"23435TwinFallsRdSiloamSprings72761OK","ID":15,"DATE_ED":"20131104","CNTY_NAME":"Adair"}},"_id":"AUo_yvdgldRc_qGNxDJr","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.50000292,35.79579132,0.0]},"properties":{"ADDRESS":"20779BelvedereEvansville72729OK","ID":1,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_ylTLldRc_qGNxDJd","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.51819102,35.90278593,0.0]},"properties":{"ADDRESS":"23655SalemSpringsNorthLincoln72744OK","ID":8,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_yqWrldRc_qGNxDJk","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.49930118,35.79206301,0.0]},"properties":{"ADDRESS":"22589StilwellEvansville72729OK","ID":10,"DATE_ED":"20140127","CNTY_NAME":"Adair"}},"_id":"AUo_ysKVldRc_qGNxDJm","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.55837819,36.13011843,0.0]},"properties":{"ADDRESS":"23423TwinFallsRdSiloamSprings72761OK","ID":13,"DATE_ED":"20131104","CNTY_NAME":"Adair"}},"_id":"AUo_yuWfldRc_qGNxDJp","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.55673216,36.12622603,0.0]},"properties":{"ADDRESS":"23439TwinFallsRdSiloamSprings72761OK","ID":16,"DATE_ED":"20131104","CNTY_NAME":"Adair"}},"_id":"AUo_ywaAldRc_qGNxDJs","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-91.35657406,34.56293716,0.0]},"properties":{"ADDRESS":"291AR33HwyRoe72134AR","ID":19,"DATE_ED":"20120605","CNTY_NAME":"Arkansas"}},"_id":"AUo_yyUPldRc_qGNxDJv","_index":"address"},{"_score":1.0,"_type":"point","_source":{"type":"Feature","geometry":{"type":"Point","coordinates":[-94.48467422,35.70736669,0.0]},"properties":{"ADDRESS":"18413NHighway59NaturalDam72948OK","ID":5,"DATE_ED":"20130117","CNTY_NAME":"Adair"}},"_id":"AUo_yozHldRc_qGNxDJh","_index":"address"}]}"""
      val totalHits = totalHitsJson.parseJson.convertTo[TotalHits]
      //totalHits.hits.foreach(println)
      totalHits.toJson.toString must be equalTo (totalHitsJson)
    }
  }

}
