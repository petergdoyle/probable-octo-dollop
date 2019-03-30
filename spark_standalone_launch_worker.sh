#!/usr/bin/env bash

log_file="$SPARK_HOME/logs/spark-vagrant-org.apache.spark.deploy.worker.Worker-1-challenge.cleverfishsoftware.com.out"
rm -v $log_file
sleep 1

$SPARK_HOME/sbin/start-slave.sh spark://challenge.cleverfishsoftware.com:7077

sleep 1
timeout 5s tail -f $log_file
