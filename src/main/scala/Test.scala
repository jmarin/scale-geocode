import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

object Test extends App {

  val client = ElasticClient.remote("192.168.59.103", 9300)

  //client.execute { index into "bands/artists" fields "name" -> "coldplay" }.await

  val resp = client.execute {
    search in "tiger" -> "line" query matchPhrase("FULLNAME", "O St NW")
  }.await
  println(resp)

}
