package com.cleverfishsoftware.challenge.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel

import java.util.regex.Pattern
import java.util.regex.Matcher

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/** Working example of listening for log data from Kafka's logs topic on port 9092. */
object SparkStreamingKafkaLogProcessor {

  def main(args: Array[String]) {

    if (args.length < 5) {
        System.err.println("Usage: SparkStreamingKafkaLogProcessor <spark-master> <brokers> <groupId> <topic1,topic2,...> <batchsize> <threshold>\n"
                + "  <spark-master> is used by the spark context to determine how to execute\n"
                + "  <brokers> is a list of one or more Kafka brokers\n"
                + "  <groupId> is a consumer group name to consume from topics\\n"
                + "  <topics> is a list of one or more kafka topics to consume from\n"
                + "  <batchsize> is a the size of the streaming batch window in seconds\\n\n"
                + "  <threshold> is a the size of the threshold to break within the batch window\\n\n");

        System.exit(1);
    }
    val master=args(0)
    val brokers=args(1)
    val groupId=args(2)
    val topics=args(3)
    val batchSize=args(4)


      // StreamingExamples.setStreamingLogLevels();

    // Create the context with a 1 second batch size
    val ssc = new StreamingContext(new SparkConf().setMaster(master).setAppName("SparkStreamingKafkaLogProcessor"), Seconds(batchSize.toLong))
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )

    /** Retrieves a regex Pattern for parsing Apache access logs. */
    def apacheLogPattern():Pattern = {
      val ddd = "\\d{1,3}"
      // val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"
      val ip = s"($ddd)\\.($ddd)\\.($ddd)\\.($ddd)?"
      val client = "(\\S+)"
      val user = "(\\S+)"
      val dateTime = "(\\[.+?\\])"
      val request = "\"(.*?)\""
      val status = "(\\d{3})"
      val bytes = "(\\S+)"
      val referer = "\"(.*?)\""
      val agent = "\"(.*?)\""
      val regex = s"$ip $client $user $dateTime $request $status $bytes $referer $agent"
      Pattern.compile(regex)
    }

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topicsSet, kafkaParams)
    )

    // create the rdd based on the values of the kafka ConsumerRecord
    val messages = stream.map(_.value)

    // Extract the request field from each log line
    val pattern = apacheLogPattern()
    val ips = messages.map(x => {val matcher:Matcher = pattern.matcher(x); if (matcher.matches()) matcher.group(1).concat(".").concat(matcher.group(2))})

    // val totals = ips.map(x => (x, 1L)).reduceByKey(_ + _)

    val totals = ips.countByValue()

    totals.print()

    // print unique ip numbers
    // val parts = messages.flatMap(_.split(" "))
    // val ips = words.map(x => (x, 1L)).reduceByKey(_ + _)
    // ips.print()


    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }

}
