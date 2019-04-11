#!/usr/bin/env bash

mvn -f challenge-java/pom.xml package && \
java -cp challenge-java/target/challenge-1.0-SNAPSHOT.jar challenge.java.RunLogParser /vagrant/data/log_data.txt
