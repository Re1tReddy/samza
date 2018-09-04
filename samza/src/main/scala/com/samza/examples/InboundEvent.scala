package com.samza.examples

// Java
import java.util.UUID

// json4s
import org.json4s._
import org.json4s.jackson.JsonMethods._

// Joda-Time
import org.joda.time.DateTime

/**
 * Companion object for creating an InboundEvent
 * from a byte array reprsenting the event JSON.
 */
object InboundEvent {

  private implicit val formats = DefaultFormats ++ ext.JodaTimeSerializers.all ++
    ext.JavaTypesSerializers.all

  /**
   * Converts Kinesis ByteArray of JSON data into SimpleEvent objects
   */
  def fromJsonBytes(byteArray: Array[Byte]): InboundEvent = {
    val newString = new String(byteArray, "UTF-8")
    val parsed = parse(newString)
    parsed.extract[InboundEvent]
  }

}

/**
 * Simple case class demonstrating an EventType log consisting of:
 *   1. ISO 8601 DateTime Object that will be downsampled
 *      (see BucketingStrategy.scala file for more details)
 *   2. A simple model of colors for this EventType:
 *      'Red','Orange','Yellow','Green', or 'Blue'
 *   example log: {"timestamp": "2015-06-05T13:00:22.540374", "type": "Orange", "id": "018dd633-f4c3-4599-9b44-ebf71a1c519f"}
 */
case class InboundEvent(id: UUID, timestamp: DateTime, `type`: String)
