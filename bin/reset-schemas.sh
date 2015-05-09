#!/bin/sh

$KAFKA_HOME/bin/schema-registry-stop

$KAFKA_HOME/bin/kafka-topics \
    --delete \
    --zookeeper localhost:2181 \
    --topic _schemas

$KAFKA_HOME/bin/schema-registry-start \
    $KAFKA_HOME/etc/schema-registry/schema-registry.properties