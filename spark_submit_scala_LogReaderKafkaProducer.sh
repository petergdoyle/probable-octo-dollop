#!/usr/bin/env bash

driver_java_opts="--driver-java-options -Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"
spark_cluster_address='spark://challenge.cleverfishsoftware.com:7077'
class_name='com.cleverfishsoftware.challenge.scala.LogReaderKafkaProducer'
jar='/vagrant/challenge-scala/target/challenge-scala-1.0-SNAPSHOT.jar'
uber_jar='/vagrant/challenge-scala-uber-jar/target/challenge-scala-uber-jar-1.0-SNAPSHOT-jar-with-dependencies.jar'
driver_java_options='--driver-java-options "-Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"'

log_file='/vagrant/data/log_data.txt'
broker_list='localhost:9092'
topic_name="logs"

skip_build="$1"
status=0
if  [ "$skip_build" != "--skipBuild" ]; then
  mvn -f challenge-scala/pom.xml package
  status=$?
fi

if test $status -eq 0; then
  spark-submit $driver_java_options --master $spark_cluster_address --jars $uber_jar --class $class_name $jar $log_file $broker_list $topic_name
fi
