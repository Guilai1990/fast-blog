To do
=====

Technical debt
--------------

* Cleanup duplication of Kafka consumers/threads
* Cleanup hardcoded strings in ElasticSearchService

Infrastructure/internal
-----------------------

* Add health checks for ZK, Kafka, Cassandra, ES
* Add instrumentation and monitoring of ZK, Cassandra, ES
* Figure out how to do integration testing of data pipeline stuff - a smoketest setup?
* Investigate blue-green deployment with HAProxy
* Investigate auto-scalng/deployment stuff with AWS + Docker or something similar

Features
--------

* Add 're-render' button to admin view to allow incremental updates of templates that get cached
* Realtime streaming analytics in admin page
* Implement publishing (only show published posts on the homepage as well)
* Implement updating
* Implement deleting

