package com.samza.examples

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties
object KafkaPush extends App {

  val TOPIC = "demo"
  //send producer message
  val jsonstring =
    s"""{
            | "timestamp":+ new java.util.Date,
            | "type": "green",
            | "id":""
            |}
         """.stripMargin

  println(jsonstring)

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")

  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  for (i <- 1 to 5) {
    val record = new ProducerRecord(TOPIC, "key", s"hello $i")
    producer.send(record)
  }

  val record = new ProducerRecord(TOPIC, "key", "the end " + new java.util.Date)
  producer.send(record)

  producer.close()

}