package parser

import org.specs2.mutable.Specification

class AddressParserSpec extends Specification {
  import model.Address

  "A correct address input" should {
    "be parsed correctly" in {
      val input = "5340 Copper Creek Ln 72223 AR"
      val address = AddressParser(input)
      address.number must be equalTo (5340)
      address.street must be equalTo ("Copper Creek Ln")
      address.zipCode must be equalTo (72223)
      address.state must be equalTo ("AR")
    }
  }

  "An incomplete address input" should {
    "throw a runtime exception" in {
      val input = "5340 Copper Creek Ln"
      AddressParser(input) must throwA[RuntimeException]
    }
  }

}
