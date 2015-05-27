#!/bin/sh

./bin/drop-recreate-es-topic.sh posts
curl -XDELETE 'http://localhost:9200/fastblog/post/?refresh=true'
curl -XDELETE 'http://localhost:9200/fastblog/?refresh=true'