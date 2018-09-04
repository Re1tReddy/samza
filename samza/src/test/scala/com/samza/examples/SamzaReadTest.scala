package com.samza.examples

import java.nio.file.Paths

import org.apache.samza.config.factories.PropertiesConfigFactory
import org.apache.samza.job.JobRunner

object SamzaReadTest extends App {
  val propertiesConfigFactory = new PropertiesConfigFactory()
  val config = propertiesConfigFactory.getConfig(Paths.get("src/main/config/samza-read.properties").toUri())
  val runner = new JobRunner(config)

  runner.run()
  while (true) {
    try {
      Thread.sleep(1000);
    } catch {
      case e: Exception => e.printStackTrace();
    }
  }
}
