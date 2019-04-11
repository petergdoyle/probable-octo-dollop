#!/usr/bin/env bash

$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092  --topic logs< $PWD/data/log_data.txt
