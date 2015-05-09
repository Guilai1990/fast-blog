#!/bin/sh

./bin/drop-recreate-es-topic.sh posts
./bin/reset-schemas.sh