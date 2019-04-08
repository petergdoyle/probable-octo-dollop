package com.cleverfishsoftware.challenge.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.sql.functions._
import org.apache.spark.sql._

import java.util.regex.Pattern
import java.util.regex.Matcher
import java.text.SimpleDateFormat
import java.util.Locale

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object SparkStreamingKafkaLogProcessorDF {

  // Case class defining structured data for a line of Apache access log data
  case class LogEntry(ip:String, subnet:String, client:String, user:String, dateTime:String, request:String, status:String, bytes:String, referer:String, agent:String)

  def main(args: Array[String]) {

    if (args.length < 8) {
        System.err.println("Usage: SparkStreamingKafkaLogProcessorDF <spark-master> <brokers> <groupId> <topic1,topic2,...> <batchsize> <threshold> <checkpointsDir> <violationsDir>\n"
                + "  <spark-master> is used by the spark context to determine how to execute\n"
                + "  <brokers> is a list of one or more Kafka brokers\n"
                + "  <groupId> is a consumer group name to consume from topics\n"
                + "  <topics> is a list of one or more kafka topics to consume from\n"
                + "  <batchsize> is a the size of the streaming batch window in seconds\n"
                + "  <threshold> is a the size of the threshold to break within the batch window\n"
                + "  <checkpointsDir> is a the fs location for spark streaming to store checkpoint information\n"
                + "  <violationsDir> is a the location for this program to write traffic violations\\n\n");
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

    val ddd = "\\d{1,3}"
    /** Retrieves a regex Pattern for parsing Apache access logs. */
    def apacheLogPattern():Pattern = {
      val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"
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
    val logPattern = apacheLogPattern()
    val datePattern = Pattern.compile("\\[(.*?) .+]") // will help out parse parts of a timestamp

    // Function to convert Apache log times to what Spark/SQL expects
    def parseDateField(field: String): Option[String] = {
    val matcher = datePattern.matcher(field)
      if (matcher.find) {
        val dateString = matcher.group(1)
        val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH)
        val date = (dateFormat.parse(dateString))
        val timestamp = new java.sql.Timestamp(date.getTime());
        return Option(timestamp.toString())
      } else {
        None
      }
    }

    val ipPartsPattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3})\\.(\\d{1,3}\\.\\d{1,3})?")
    // Function to extact the subnet from the ip
    def parseSubnetField(field: String): Option[String] = {
      val matcher = ipPartsPattern.matcher(field)
      if (matcher.find) {
        val part1 = matcher.group(1) // this should be the subnet
        val part2 = matcher.group(2)
        return Option(part1)
      } else {
        None
      }
    }

    // Convert a raw line of Apache access log data to a structured LogEntry object (or None if line is corrupt)
    def parseLog(x:Row) : Option[LogEntry] = {
      val s = Option(x.getString(0)).getOrElse("")
      val matcher:Matcher = logPattern.matcher(s);
      if (matcher.matches()) {
        return Some(LogEntry(
        matcher.group(1),
        parseSubnetField(matcher.group(1)).getOrElse(""),
        matcher.group(2),
        matcher.group(3),
        parseDateField(matcher.group(4)).getOrElse(""),
        matcher.group(5),
        matcher.group(6),
        matcher.group(7),
        matcher.group(8),
        matcher.group(9)
        ))
      } else {
        return None
      }
    }

    val spark = SparkSession
      .builder
      .appName("SparkStreamingKafkaLogProcessorDF")
      .getOrCreate()

    import spark.implicits._

    // Create DataSet representing the stream of input lines from kafka
    val rawData = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", brokers)
      .option("subscribe", topics)
      .load()
      .selectExpr("CAST(value AS STRING)")
      // .as[String]

    // Convert our raw text into a DataSet of LogEntry rows, then just select the two columns we care about
    val structuredData = rawData.flatMap(parseLog).select("subnet", "dateTime")

    // Group by subnet code, with a one-hour window.
    val windowed = structuredData
    .groupBy($"subnet",window($"dateTime", "2 second"))
    .count()
    .orderBy("window")

    // Start the streaming query, dumping results to the console. Use "complete" output mode because we are aggregating
    // (instead of "append").
    val query = windowed.writeStream
      .outputMode("complete")
      .format("console")
      .option("checkpointLocation",checkpointDir)
      .start()

    // Keep going until we're stopped.
    query.awaitTermination()

    spark.stop()

  }

}
