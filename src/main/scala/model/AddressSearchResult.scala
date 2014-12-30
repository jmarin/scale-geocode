package model

import feature._

case class Shard(total: Int, successful: Int, failed: Int)
case class AddressProperties(ID: Int, CNTY_NAME: String, DATE_ED: String, ADDRESS: String)
//case class Source(feature: Feature)
case class Hits(_index: String, _type: String, _id: String, _score: Double, _source: Feature)
case class TotalHits(total: Int, max_score: Double, hits: Array[Hits])
case class AddressSearchResult(took: Int, timed_out: Boolean, _shards: Shard, hits: TotalHits)
case class AddressOutput(f: Feature)
