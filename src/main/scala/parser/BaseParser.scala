package parser

import scala.util.parsing.combinator._

trait BaseParser extends JavaTokenParsers {

  val str: Parser[String] = """[a-zA-Z0-9_]+""".r ^^ { _.toString }

  val word: Parser[String] = """[a-zA-Z]+""".r ^^ { _.toString }

  val int: Parser[Int] = wholeNumber ^^ { _.toInt }

  val long: Parser[Long] = wholeNumber ^^ { _.toLong }

  val dot: Parser[Any] = "."

  val hiphen: Parser[Any] = "-"

}
