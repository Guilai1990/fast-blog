#!/usr/bin/env bash

# Deletes a given topic

$KAFKA_HOME/bin/kafka-topics.sh \
    --delete \
    --zookeeper localhost:2181 \
    --topic $1