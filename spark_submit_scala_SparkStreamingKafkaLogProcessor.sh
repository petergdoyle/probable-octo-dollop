#!/usr/bin/env bash

spark_cluster_address='spark://challenge.cleverfishsoftware.com:7077'
class_name='com.cleverfishsoftware.challenge.scala.SparkStreamingKafkaLogProcessor'
jar='/vagrant/challenge-scala/target/challenge-scala-1.0-SNAPSHOT.jar'
driver_java_options='--driver-java-options "-Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"'
broker_list='localhost:9092'
topic_name="logs"
consumer_group_id="SparkStreamingKafkaLogProcessor-cg"
batch_size="1"
uber_jar='/vagrant/challenge-scala-uber-jar/target/challenge-uber-jar-1.0-SNAPSHOT-jar-with-dependencies.jar'

# val master=args(0)
# val brokers=args(1)
# val groupId=args(2)
# val topics=args(3)
# val batchSize=args(4)

mvn -f challenge-scala/pom.xml package && \
spark-submit $driver_java_options --master $spark_cluster_address --jars $uber_jar --class $class_name $jar $spark_cluster_address $broker_list $consumer_group_id $topic_name $batch_size
