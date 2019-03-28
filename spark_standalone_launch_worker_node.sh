#!/usr/bin/env bash
$SPARK_HOME/bin spark-class org.apache.spark.deploy.worker.Worker  spark://localhost:7077 -c 4 -m 2048
