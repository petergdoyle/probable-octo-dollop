# Project Title

Build a realtime streaming process to detect anomalies in streaming data sets using Kafka and Spark Streaming, taking advantage of features found in Spark Structured Streaming and Spark MLLib. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- **Vagrant and VitualBox **installed on a PC or Server (recommended) 

**++OR++** self-configured installation of the following: 

- **any RPM-based Linux Distro with a Bash Shell** - developed and tested with CentOS 7.1, other distros may or may not work without modifications
- **Java JDK-8.*** with a ==JAVA_HOME== environment varible set and $JAVA_HOME/bin defined in the PATH
- **Apache Maven 3.6.*** Installed with a ==MAVEN_HOME== environment variable set and $MAVEN_HOME/bin defined in the PATH
- **Scala-2.11.*** Installed with a ==SCALA_HOME== environment variable set and $SCALA_HOME/bin defined in the PATH
- **Spark spark-2.4.*** Installed with a ==SPARK_HOME== environment variable set and $SPARK_HOME/bin defined in the PATH
- Kafka 10.2.1 Installed with a ==KAFKA_HOME== environment variable set and $KAFKA_HOME/bin defined in the PATH
- **Optional:** Docker, Docker-Compose 

Check project websites for detailed instructions about how to install any of the pre-requisites. 

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

### Installing


Recommended Setup is to use Vagrant and Virtualbox to set p th

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
