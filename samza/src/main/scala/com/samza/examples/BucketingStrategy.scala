package com.samza.examples

// Joda-Time
import org.joda.time.{ DateTime, DateTimeZone }
import org.joda.time.format.DateTimeFormat

object BucketingStrategy {
  private val BucketToMinuteFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:00.000").withZone(DateTimeZone.UTC)

  /**
   * Function to bucket a date based on
   * our bucketing strategy. Bucketing
   * means downsampling aka reducing
   * precision.
   *
   * @param timestamp The DateTime to bucket
   * @return the downsampled timestamp in String
   *         format
   */
  def bucket(timestamp: DateTime): String =
    BucketToMinuteFormatter.print(timestamp)
}