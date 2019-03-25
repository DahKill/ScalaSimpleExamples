package me.amuxix
import play.api.libs.json.{JsValue, Json, Reads}

/**
  * This creates a simple case class that has a single field.
  *
  * A case class differs from a normal class in the way that a case class a few methods implemented for you
  * The
  *
  * @param int
  */
case class FancyInt(int: Int, a: String) {
  def +(other: FancyInt): FancyInt = FancyInt(int + other.int, "")
}

object FancyInt {
  def a(a: Int, b: Int, c: Int): FancyInt = FancyInt((a to b).foldLeft(0) { (acc, i) =>
    acc + i * c
  }, "")

}
