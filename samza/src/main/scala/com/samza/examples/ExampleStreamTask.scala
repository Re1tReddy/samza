package com.samza.examples

import collection.mutable.HashSet
import org.apache.samza.config.Config
import org.apache.samza.system.IncomingMessageEnvelope
import org.apache.samza.system.SystemStream
import org.apache.samza.task.InitableTask
import org.apache.samza.task.MessageCollector
import org.apache.samza.task.StreamTask
import org.apache.samza.task.TaskContext
import org.apache.samza.task.TaskCoordinator
import org.apache.samza.task.WindowableTask
import org.apache.samza.system.OutgoingMessageEnvelope
import collection.mutable.HashSet
import org.apache.samza.storage.kv.KeyValueStore

/**
 * This task is very simple. All it does is take messages that it receives, and
 * sends them to a Kafka topic called wikipedia-raw.
 */
class ExampleStreamTask extends StreamTask with InitableTask with WindowableTask {

  type CountStore = KeyValueStore[String, Integer]
  type KeySet = HashSet[String]
  val AsciiUnitSeparator = "\u001f"
  private var store: CountStore = _
  private var updatedKeys: KeySet = _

  /**
   * One-off initialization for our task.
   */
  def init(config: Config, context: TaskContext) {
    this.store = context.getStore("example-project").asInstanceOf[CountStore]
    this.updatedKeys = new KeySet()
  }

  /**
   * Invoked for each incoming event.
   */
  override def process(
    envelope:  IncomingMessageEnvelope,
    collector: MessageCollector, coordinator: TaskCoordinator) {

    // TODO: we could implement our own Json4s Serde for Samza instead
    val event = {
      val bytes = envelope.getMessage.asInstanceOf[Array[Byte]]
      InboundEvent.fromJsonBytes(bytes)
    }

    val bucket = BucketingStrategy.bucket(event.timestamp)
    val key = List(bucket, event.`type`).mkString(AsciiUnitSeparator)
    val count: Integer = Option(store.get(key)).getOrElse(0)

    store.put(key, count + 1)
    updatedKeys.add(key)
  }

  /**
   * Invoked every N seconds.
   */
  override def window(collector: MessageCollector, coordinator: TaskCoordinator) {
   println("window called at : " + new java.util.Date)
    val event = {
      val counts = updatedKeys
        .map(k => (k -> store.get(k)))
        .toMap
      WindowSummaryEvent(counts)
    }

    println("Event : "+event.toString())
    collector.send(new OutgoingMessageEnvelope(
      new SystemStream("kafka", "events"), event.toJsonBytes))

    updatedKeys.clear()
  }
}
