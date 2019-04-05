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
- **Kafka 10.2.1** Installed with a KAFKA_HOME environment variable set and $KAFKA_HOME/bin defined in the PATH 
	- recommend to clone a copy of https://github.com/petergdoyle/kafka-proxied to install, configure and manage a small standalone Kafka cluster which supports Kafka-10.2.1 
- **Optional:** Docker, Docker-Compose (not recommended at this time)

**Check project websites for detailed instructions about how to install any of the pre-requisites.** 

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

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo


