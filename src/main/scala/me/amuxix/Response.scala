package me.amuxix
import play.api.libs.json.{Format, Json, Reads, Writes}

case class Lines (
  currencyTypeName: String,
  chaosEquivalent: Double,
)

object Lines {
  implicit val reads: Reads[Lines] = Json.reads
}


case class Currency (
  lines: Seq[Lines],
)

object Currency {
  implicit val reads: Reads[Currency] = Json.reads
}
