package model

import geometry.Geometry

case class Shard(total: Int, successful: Int, failed: Int)
case class AddressProperties(ID: Int, CNTY_NAME: String, DATE_ED: String, ADDRESS: String)
case class Source(`type`: String, properties: AddressProperties, geometry: Geometry)
case class Hits(_index: String, _type: String, _id: String, score: Double, _source: Source)
case class TotalHits(total: Int, max_score: Double, hits: Array[Hits])
case class AddressSearchResult(took: Int, timed_out: Boolean, shards: Shard, hits: TotalHits)
