
java_class='challenge.java.spark.KafkaSubnetCount'

# mvn -T 1C -f challenge/pom.xml --offline install  && \
topic_name="subnets"
consumer_group_id="subnet-count-consumer-group"
spark-submit --driver-java-options "-Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"  --class $java_class /vagrant/challenge/target/challenge-1.0-SNAPSHOT-jar-with-dependencies.jar localhost:9092 $consumer_group_id $topic_name
