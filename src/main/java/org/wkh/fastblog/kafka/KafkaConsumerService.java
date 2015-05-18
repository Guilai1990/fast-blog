/* Originally written by Confluent: https://github.com/confluentinc/examples/blob/master/consumer/src/main/java/io/confluent/examples/consumer/ConsumerGroupExample.java
Original license follows
 */

/**
 * Copyright 2015 Confluent Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wkh.fastblog.kafka;

import kafka.consumer.ConsumerConfig;import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import java.util.HashMap;import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.cassandra.CassandraPostDAO;
import org.wkh.fastblog.serialization.SerializationService;

import javax.validation.constraints.NotNull;

@Service
@ConfigurationProperties(prefix="kafka")
public class KafkaConsumerService implements InitializingBean, DisposableBean {
    private Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @NotNull
    private String zookeeper;

    @NotNull
    private String groupId;

    @NotNull
    private String consumerReset;

    @NotNull
    private Integer threadCount;

    private ConsumerConnector consumer;

    private ExecutorService executor;

    @NotNull
    private String postsTopic;

    @Autowired
    private SerializationService serializationService;

    @Autowired
    private CassandraPostDAO dao;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (postsTopic == null) {
            log.error("postsTopic is null! Can't make Kafka consumer!");
            return;
        } else {
            log.info("postsTopic set to: " + postsTopic);
        }

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(createConsumerConfig()));

        run();
    }

    private Properties createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("auto.offset.reset", consumerReset);

        return props;
    }

    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(postsTopic, threadCount);

        Properties props = createConsumerConfig();

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
                consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(postsTopic);

        // Launch all the threads
        executor = Executors.newFixedThreadPool(threadCount);

        // Create PostConsumerThread objects and bind them to threads
        for (final KafkaStream stream : streams) {
            executor.submit(new CassandraPostConsumerThread(stream, serializationService, consumer, dao));
        }
    }

    @Override
    public void destroy() throws Exception {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                log.error("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            log.error("Interrupted during shutdown, exiting uncleanly");
        }
    }



    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setConsumerReset(String consumerReset) {
        this.consumerReset = consumerReset;
    }

    public void setPostsTopic(String postsTopic) {
        this.postsTopic = postsTopic;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }
}
