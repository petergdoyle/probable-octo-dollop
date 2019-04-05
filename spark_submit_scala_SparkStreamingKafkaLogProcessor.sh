#!/usr/bin/env bash

rdd_class_name='com.cleverfishsoftware.challenge.scala.SparkStreamingKafkaLogProcessorRDD'
df_class_name='com.cleverfishsoftware.challenge.scala.SparkStreamingKafkaLogProcessorDF'

class_name="$rdd_class_name"
spark_cluster_address='spark://challenge.cleverfishsoftware.com:7077'
jar='/vagrant/challenge-scala/target/challenge-scala-1.0-SNAPSHOT.jar'
uber_jar='/vagrant/challenge-scala-uber-jar/target/challenge-scala-uber-jar-1.0-SNAPSHOT-jar-with-dependencies.jar'
driver_java_options='--driver-java-options "-Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"'

broker_list='localhost:9092'
topic_name="logs"
consumer_group_id="SparkStreamingKafkaLogProcessor-cg"
batch_size="1"

skip_build="$1"
status=0
if  [ "$skip_build" != "--skipBuild" ]; then
  mvn -f challenge-scala/pom.xml package
  status=$?
fi

if test $status -eq 0; then
  spark-submit $driver_java_options --master $spark_cluster_address --jars $uber_jar --class $class_name $jar $spark_cluster_address $broker_list $consumer_group_id $topic_name $batch_size
fi
