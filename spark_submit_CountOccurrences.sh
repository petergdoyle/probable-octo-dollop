mvn -f challenge/pom.xml clean install && spark-submit --class challenge.java.spark.CountOccurrences /vagrant/challenge/target/challenge-1.0-SNAPSHOT-jar-with-dependencies.jar /vagrant/data/log_data.txt