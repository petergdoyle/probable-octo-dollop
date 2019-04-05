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

object SparkStreamingKafkaLogProcessorRDD {

  def main(args: Array[String]) {

    if (args.length < 8) {
        System.err.println("Usage: SparkStreamingKafkaLogProcessorRDD <spark-master> <brokers> <groupId> <topic1,topic2,...> <batchsize> <threshold> <checkpointsDir> <violationsDir>\n"
                + "  <spark-master> is used by the spark context to determine how to execute\n"
                + "  <brokers> is a list of one or more Kafka brokers\n"
                + "  <groupId> is a consumer group name to consume from topics\\n"
                + "  <topics> is a list of one or more kafka topics to consume from\n"
                + "  <batchsize> is a the size of the streaming batch window in seconds\\n\n"
                + "  <threshold> is a the size of the threshold to break within the batch window\\n\n"
                + "  <checkpointsDir> is a the fs location for spark streaming to store checkpoint information\\n\n"
                + "  <violationsDir> is a the location for this program to write traffic violations to\\n\n");
        System.exit(1)
    }

    val master=args(0)
    val brokers=args(1)
    val groupId=args(2)
    val topics=args(3)
    val batchSize=args(4)
    val threshold=args(5)
    val checkpointDir=args(6)
    var violationsDir=args(7)

    // Create the context with specified batch size
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

    val pattern = apacheLogPattern()

    // extract the subnets / ips from the raw log files
    // val subnets = messages.map(x => {val matcher:Matcher = pattern.matcher(x); if (matcher.matches()) matcher.group(1).concat(".").concat(matcher.group(2)).concat(".*.*")})
    val ips = messages.map(x => {val matcher:Matcher = pattern.matcher(x); if (matcher.matches()) matcher.group(1).concat(".").concat(matcher.group(2)).concat(".").concat(matcher.group(3)).concat(".").concat(matcher.group(4))})

    // Now count them up over a 3 second window sliding every 1 second
    val vals = ips.map(x => (x, 1))
    val windowLength=Seconds(3)
    val slideInterval=Seconds(1)
    val counts = vals.reduceByKeyAndWindow( (x,y) => x + y, (x,y) => x - y, windowLength, slideInterval)
    //shorthand:
    //val counts = vals.reduceByKeyAndWindow( _ + _, _ -_, windowLength, slideInterval)

    // val orderedViolations = counts.transform(rdd => rdd.sortBy(x => x._2, false)).filter(x => x._2 >=threshold.toInt)
    // find the threshold violations by ordering the totals and filtering out the ones that don't exceed the threshold level
    val violations = counts.filter(x => x._2 >=threshold.toInt)
    violations.print() // just to see something at the console

    // iterate through violations and print to file
    violations.foreachRDD { rdd =>
      if (!rdd.isEmpty) {
        rdd.saveAsTextFile(violationsDir)
      }
    }

    // Start the computation
    ssc.checkpoint(checkpointDir)
    ssc.start()
    ssc.awaitTermination()
  }

}
