#!/bin/sh

./bin/drop-topic.sh $1
./bin/create-es-topic.sh $1