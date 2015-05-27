#!/usr/bin/env bash

# Creates a log-compacted topic at the default ZooKeeper host.

$KAFKA_HOME/bin/kafka-topics.sh \
    --create \
    --zookeeper localhost:2181 \
    --replication-factor 1 \
    --partitions 1 \
    --topic $1 \
    --config cleanup.policy=compact