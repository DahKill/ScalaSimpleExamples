package me.amuxix

class Cenas(fancyInt: FancyInt) {
  implicit class FancyIntOps(fancyInt: FancyInt) {
    def stringyfy: String = fancyInt.a * fancyInt.int
  }

  val int: FancyInt = ???

  int.stringyfy
}
