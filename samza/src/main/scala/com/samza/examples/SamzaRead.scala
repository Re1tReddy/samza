package com.samza.examples

import org.apache.samza.config.Config
import org.apache.samza.system.IncomingMessageEnvelope
import org.apache.samza.system.SystemStream
import org.apache.samza.task.InitableTask
import org.apache.samza.task.MessageCollector
import org.apache.samza.task.StreamTask
import org.apache.samza.task.TaskContext
import org.apache.samza.task.TaskCoordinator
import org.apache.samza.task.WindowableTask
import org.slf4j.LoggerFactory

class SamzaRead extends InitableTask with StreamTask with WindowableTask {

  val log = LoggerFactory.getLogger(this.getClass)

  val amonOutputStream = new SystemStream("kafka", "test")

  def init(conf: Config, taskContext: TaskContext): Unit = {
    println("init called at : " + new java.util.Date)
  }

  override def process(envelope: IncomingMessageEnvelope, collector: MessageCollector, coordinator: TaskCoordinator): Unit = {
    println("process called at : " + new java.util.Date)
    val topic = envelope.getSystemStreamPartition.getSystemStream.getStream
    println("processing data for topic : " + topic)
    val event = {
      val bytes = envelope.getMessage.asInstanceOf[Array[Byte]]
      InboundEvent.fromJsonBytes(bytes)
    }
    println(event.toString())

  }

  override def window(collector: MessageCollector, coordinator: TaskCoordinator): Unit = {
    println("window called at : " + new java.util.Date)
  }

}
