package com.samza.examples

import java.util.UUID
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write
import org.joda.time.{ DateTime, DateTimeZone }

object WindowSummaryEvent {
  type CountMap = Map[String, Integer]

  private implicit val formats = DefaultFormats ++ ext.JodaTimeSerializers.all ++
    ext.JavaTypesSerializers.all

  def apply(counts: CountMap): WindowSummaryEvent = {
    val id = UUID.randomUUID()
    val ts = new DateTime(DateTimeZone.UTC)
    WindowSummaryEvent(id, ts, counts)
  }

  def toJsonBytes(wse: WindowSummaryEvent): Array[Byte] =
    write(wse).getBytes
}

case class WindowSummaryEvent(id: UUID, timestamp: DateTime, counts: Map[String, Integer]) {

  def toJsonBytes: Array[Byte] =
    WindowSummaryEvent.toJsonBytes(this)
}