# Probable-Octo-Dollop

The name here means nothing but the purpose is to build a realtime streaming process to detect anomalies in streaming data sets using Kafka and Spark Streaming, integrate some of the features found in Spark Structured Streaming and Spark MLLib for more advanced stream processing and 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- **Vagrant and VitualBox** installed on a PC or Server (recommended) 

**++OR++** self-configured installation of the following: 

- **any RPM-based Linux Distro with a Bash Shell** - developed and tested with` CentOS Linux release 7.6 (Core)`, other distros may or may not work without modifications
- **Java JDK-8.*** with a JAVA_HOME environment varible set and $JAVA_HOME/bin defined in the PATH
- **Apache Maven 3.6.*** Installed with a MAVEN_HOME environment variable set and $MAVEN_HOME/bin defined in the PATH
- **Scala-2.11.*** Installed with a SCALA_HOME environment variable set and $SCALA_HOME/bin defined in the PATH
- **Spark spark-2.4.*** Installed with a SPARK_HOME environment variable set and $SPARK_HOME/bin defined in the PATH
- **kafka_2.11-0.10.2.1** Installed with a KAFKA_HOME environment variable set and $KAFKA_HOME/bin defined in the PATH 
	- recommend to clone a copy of https://github.com/petergdoyle/kafka-proxied to install, configure and manage a small standalone Kafka cluster which supports kafka_2.11-0.10.2.1 and run `$ install/install_kafka.sh`
- **Optional:** Docker, Docker-Compose (not recommended at this time)

**Check project websites for detailed instructions about how to download and install any of the pre-requisites.** 

You should be able to see the following once everything is available in your system (either building from the .Vagrantfile provided here or on your own pre-configured system)

```
$ env |grep HOME
KAFKA_HOME=/vagrant/kafka-proxied/local/kafka/default
SPARK_HOME=/usr/spark/default
MAVEN_HOME=/usr/maven/default
JAVA_HOME=/usr/java/default
HOME=/home/vagrant
SCALA_HOME=/usr/scala/default

$ echo $PATH
/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin:$MAVEN_HOME/bin:$SCALA_HOME/bin:$SPARK_HOME/bin:/home/vagrant/.local/bin:/home/vagrant/bin:$KAFKA_HOME/bin

```

### Running the Code   


Recommended Setup is to use Vagrant and Virtualbox to set up a virtual machine provisioned with all the Prerequisites detailed above. 

Create a Kafka Standalone Cluster (using kafka-proxied scripts) and create topics as follows (pay attention the the required setup values shown below) and check the last output to make sure the cluster looks good (broker and zookeeper processes are running).

```
$ cd kafka-proxied/install
$ ./all_in_one_standup.sh 
[info] Kafka version is '0.10.2.1'.
Enter the number of zookeeper instances: 1
Enter the zookeeper host: localhost
Enter the zookeeper host port: 2181
‘/vagrant/kafka-proxied/kafka/config/0.10.2.1/zookeeper-template.properties’ -> ‘/vagrant/kafka-proxied/local/kafka/kafka_2.11-0.10.2.1/config/challenge-zookeeper-1-config.properties’
[info] /vagrant/kafka-proxied/local/kafka/default/bin/zookeeper-server-start.sh /vagrant/kafka-proxied/local/kafka/kafka_2.11-0.10.2.1/config/challenge-zookeeper-1-config.properties> /vagrant/kafka-proxied/local/kafka/kafka_2.11-0.10.2.1/logs/challenge-zookeeper-1-console.log 2>&1
About to start Zookeeper instance 1, continue? (y/n): y
...
[info] Kafka version is '0.10.2.1'.
[info] 0 broker process running.
Enter the number of broker instances: 1
Confirm the Broker Id (must be unique INT within the cluster): 1
‘/vagrant/kafka-proxied/kafka/config/0.10.2.1/broker-template.properties’ -> ‘/vagrant/kafka-proxied/local/kafka/kafka_2.11-0.10.2.1/config/challenge-broker-1.properties’
Enter the broker port: 9092
Enter the the address the socket server listens on (locally): PLAINTEXT://:9092
Will the broker be accessed by a proxy or external public server (y/n)?: n
Enter the zookeeper host: localhost
Enter the zookeeper host port: 2181
Specify maximum message size the broker will accept (message.max.bytes) in MB. Default value (1 MB): 1
You must make sure that the Kafka consumer configuration parameter fetch.message.max.bytes is specified as at least 1048576!
Specify Size of a Kafka data file (log.segment.bytes) in GiB. Must be larger than any single message. Default value: (1 GiB): 1
Enter kafka Log default Retention Time( hours, minutes, ms ): hours
Enter Kafka Log default Retention hours: 1
Enter Kafka Log default Retention Size (Mb): 25
[info] /vagrant/kafka-proxied/local/kafka/default/bin/kafka-server-start.sh /vagrant/kafka-proxied/local/kafka/kafka_2.11-0.10.2.1/config/challenge-broker-1.properties > /vagrant/kafka-proxied/local/kafka/kafka_2.11-0.10.2.1/logs/challenge-broker-1-console.log 2>&1
About to start Kafka Broker, continue? (y/n): y
... 
About to create new Kafka Topic? (y/n): y
Enter the zk host/port: localhost:2181
Enter the topic name: logs
Enter the number of partitions: 4
Enter the replication factor: 1
Enter topic retention time (hrs): 1
Enter topic retention size (Mb): 25
Enter topic max message size (Kb): 256
[info] /vagrant/kafka-proxied/local/kafka/default/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 4 --topic logs --config max.message.bytes=262144 --config retention.bytes=26214400 --config retention.ms=3600000
About to start Create Topics as shown, continue? (y/n): y
...
[info] Kafka Cluster Status:
[info] Zookeeper process(es) running:
638
[info] Kafka Broker process(es) running:
943
```
`cd` back into the directory where kafka-proxied is located (`cd ~` if you are using the VM image or it was put in your home dir) and you can run the following:

**Check the Status of the Kafka Cluster** 

```
$ ./kafka_check_status.sh
...
[info] Kafka Cluster Status:
[info] Zookeeper process(es) running:
638
[info] Kafka Broker process(es) running:
943
[warn] Mirror-Maker process(es) running: No Mirror-Maker processes running

```

End with an example of getting some data out of the system or using it for a little demo


