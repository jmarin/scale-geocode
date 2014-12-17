package model

import spray.json._

object AddressJsonProtocol extends DefaultJsonProtocol with NullOptions {

  import geojson.FeatureJsonProtocol._

  implicit val shardFormat = jsonFormat3(Shard)
  implicit val addressPropertiesFormat = jsonFormat4(AddressProperties)
  implicit val hitsFormat = jsonFormat5(Hits)
  implicit val totalHitsFormat = jsonFormat3(TotalHits)
  implicit val addressSearchResultFormat = jsonFormat4(AddressSearchResult)
}

