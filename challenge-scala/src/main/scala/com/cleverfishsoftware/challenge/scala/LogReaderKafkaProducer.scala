package com.cleverfishsoftware.challenge.scala

import scala.io.Source

/** Read from a web log and push lines read into kafka */
object LogReaderKafkaProducer {

  def main(args: Array[String]) {

    if (args.length == 0) {
        System.err.println("args required: \"local log file name\", \"kafka broker list\", \"kafka topic name\"")
        System.exit(1);
    }

    import java.util.Properties
    import org.apache.kafka.clients.producer._

    val  props = new Properties()

    val filename = args(0)
    val kafkaBrokers = args(1)
    val kafkaTopic = args(2)

    props.put("bootstrap.servers", kafkaBrokers)

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    for (line <- Source.fromFile(filename).getLines) {
      // println(line)
      producer.send(new ProducerRecord[String, String](kafkaTopic, line))
    }

  }

}
