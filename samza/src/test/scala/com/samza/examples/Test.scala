package com.samza.examples

import scala.collection.{ mutable, immutable }

object Test extends App {
  var tid: Int = 0
  val cond = immutable.Map("tid" -> tid.asInstanceOf[AnyRef])
  val fields =
    immutable.Map(
      "bssid" -> 1.asInstanceOf[AnyRef],
      "ssid" -> 1.asInstanceOf[AnyRef],
      "apmac" -> 1.asInstanceOf[AnyRef])
  println(fields)
}