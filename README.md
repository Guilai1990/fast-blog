fast-blog
=========

An attempt at making a high-performance blog engine that makes extensive use of caching.

The current attempt is to try to use [Apache Kafka](http://kafka.apache.org/) to store data and then [generate materialized views off of it using a stream 
processor](http://blog.confluent.io/2015/03/04/turning-the-database-inside-out-with-apache-samza/).

Currently this is extremely early in its development and basically useless. Check back in a couple weeks or months! :)

This project is some extent merely a testbed for playing with new and old techologies, including:

* [Spring Boot](http://projects.spring.io/spring-boot/)
* [Apache Kafka](http://kafka.apache.org/)
* [Apache Avro](https://avro.apache.org/)
* [Apache Cassandra](http://cassandra.apache.org/)

Usage of [Apache Solr](http://lucene.apache.org/solr/) and [Apache Spark](http://spark.apache.org) is planned.
The shell scripts in `bin` assume you have an environment variable `$KAFKA_HOME` set to the installation directory
of the [Confluent Platform](http://confluent.io/docs/current/index.html). Follow the 
[quickstart](http://confluent.io/docs/current/quickstart.html) they have to install and run ZooKeeper and Kafka.
You'll need to run Kafka (and therefore ZooKeeper) to start up the application. You'll also need Cassandra running.

Make a topic that's [log-compacted](https://cwiki.apache.org/confluence/display/KAFKA/Log+Compaction) called posts:

```
$ bin/create-es-topic.sh posts
```

The "ES" in the script file name refers to "[event sourced](http://martinfowler.com/eaaDev/EventSourcing.html)".

Edit `application.properties` with any custom Kafka configuration values you have.
If you're just using the out of the box defaults, all you should need to enter is your PostgreSQL settings.

You'll need [Maven](https://maven.apache.org/) installed. Then you can build the project like so:

```
$ mvn clean package
```

Then (assuming the build succeeds) run the JAR it creates:

```
java -jar target/fast-blog-1.0-SNAPSHOT.jar --security.user.password=your-password
```

The build is set up to build a single, fat JAR. This JAR contains all the dependencies for the project, including an 
embedded [Tomcat](http://tomcat.apache.org/) instance! :)

Go to `http://localhost:8080/posts/new` and try making a post. You'll be prompted to authenticate. The default
username is set to be `admin`. The password is whatever you provided at the
command line with `security.user.password`.

The `bin` directory has helper scripts you may find useful.