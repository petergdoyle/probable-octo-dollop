#!/usr/bin/env bash

$KAFKA_HOME/bin/kafka-console-consumer.sh --new-consumer --bootstrap-server localhost:9092 --topic challenge-log-in  --from-beginning --delete-consumer-offsets
