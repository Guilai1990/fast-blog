#!/usr/bin/env bash

# Deletes a given topic

$KAFKA_HOME/bin/kafka-topics \
    --delete \
    --zookeeper localhost:2181 \
    --topic $1