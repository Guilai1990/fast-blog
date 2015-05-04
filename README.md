fast-blog
=========

An attempt at making a high-performance blog engine that makes extensive use of caching.

The current attempt is to try to use [Apache Kafka](http://kafka.apache.org/) to store data and then [generate materialized views off of it using a stream 
processor](http://blog.confluent.io/2015/03/04/turning-the-database-inside-out-with-apache-samza/).

Currently this is extremely early in its development and basically useless. Check back in a couple weeks or months! :)

The shell scripts in `bin` assume you have an environment variable `$KAFKA_HOME` set to the installation directory
of the [Confluent Platform](http://confluent.io/docs/current/index.html). Follow the 
[quickstart](http://confluent.io/docs/current/quickstart.html) they have to install and run ZooKeeper, Kafka, and the 
Schema Registry. You'll need to be running all three to create posts.

Make a topic that's [log-compacted](https://cwiki.apache.org/confluence/display/KAFKA/Log+Compaction) called posts:

```
$ bin/create-es-topic.sh posts
```

The "ES" in the script file name refers to "[event sourced](http://martinfowler.com/eaaDev/EventSourcing.html)".

Then run the JAR:

```
java -jar target/fast-blog-1.0-SNAPSHOT.jar
```

Go to `http://localhost:8080/posts/new` and try making a post. Running a `kafka-avro-console-consumer` the way the 
quickstart indicates should show posts as they're created.