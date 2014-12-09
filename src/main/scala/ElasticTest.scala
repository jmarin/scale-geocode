import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

object Test extends App {

  val client = ElasticClient.remote("192.168.59.103", 9300)

  val res = client.execute { search in "address/point" query "OK" }.await
  println(res)
}
