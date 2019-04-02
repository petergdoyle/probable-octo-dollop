#!/usr/bin/env bash

driver_java_opts="--driver-java-options -Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"
spark_cluster_address='spark://challenge.cleverfishsoftware.com:7077'
class_name='com.cleverfishsoftware.challenge.scala.LogReaderKafkaProducer'
jar='/vagrant/challenge-scala/target/challenge-scala-1.0-SNAPSHOT.jar'
uber_jar='/vagrant/challenge-scala-uber-jar/target/challenge-uber-jar-1.0-SNAPSHOT-jar-with-dependencies.jar'

log_file='/vagrant/data/log_data.txt'
broker_list='localhost:9092'
topic_name="logs"

mvn -f challenge-scala/pom.xml package && \
spark-submit --master $spark_cluster_address --jars $uber_jar --class $class_name $jar $log_file $broker_list $topic_name
