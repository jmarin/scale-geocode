package parser

import model.Address

object AddressParser extends BaseParser {

  val number: Parser[Int] = int

  val street: Parser[List[String]] = rep(word)

  val zipCode: Parser[Int] = int

  val state: Parser[String] =
    """^[a-zA-Z]{2}$""".r ^^ { _.toString }

  val address = number ~ street ~ zipCode ~ state ^^ {
    case (number ~ street ~ zipCode ~ state) =>
      Address(number, street.mkString(" "), zipCode, state)
  }

  def apply(input: String): Address = parseAll(address, input) match {
    case Success(result, _) => result
    case Failure(msg, _) => throw new RuntimeException(msg)
    case Error(msg, _) => throw new RuntimeException(msg)
  }

}
