#!/usr/bin/env bash

mvn -f challenge/pom.xml -Pwithout-assembly clean install && \
java -cp challenge/target/challenge-1.0-SNAPSHOT.jar challenge.java.RunKafkaMessageSender /vagrant/data/log_data.txt
