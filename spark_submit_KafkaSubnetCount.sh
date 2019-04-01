
java_class='challenge.java.spark.KafkaSubnetCount'
jar_name='/vagrant/challenge/target/challenge-1.0-SNAPSHOT-jar-with-dependencies.jar'
broker_list='localhost:9092'
topic_name="subnets"
consumer_group_id="subnet-count-consumer-group"
# mvn -T 1C -f challenge/pom.xml --offline install  && \
spark-submit \
  --master spark://localhost:7077 \
  --driver-java-options "-Dlog4j.configuration=file:///vagrant/spark_log4j_QUIET.properties"  \
  --class $java_class $jar_name $broker_list $consumer_group_id $topic_name
