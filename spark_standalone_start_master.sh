#!/usr/bin/env bash


echo "removing old log file..."
sleep 1
log_file="$SPARK_HOME/logs/spark-vagrant-org.apache.spark.deploy.master.Master-1-challenge.cleverfishsoftware.com.out"
rm -v $log_file
sleep 1

echo "starting master..."
sleep 1
$SPARK_HOME/sbin/start-master.sh

echo "tailing on log for 5s..."
sleep 1
timeout 5s tail -f $log_file
