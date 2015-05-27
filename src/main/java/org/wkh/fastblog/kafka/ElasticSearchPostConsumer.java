package org.wkh.fastblog.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.elasticsearch.ElasticSearchService;
import org.wkh.fastblog.serialization.SerializationService;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@ConfigurationProperties(prefix="kafka")
public class ElasticSearchPostConsumer implements InitializingBean, DisposableBean {
    private Logger log = LoggerFactory.getLogger(ElasticSearchPostConsumer.class);

    @NotNull
    private String zookeeper;

    @NotNull
    private String elasticSearchGroupId;

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
    private ElasticSearchService elasticSearch;

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(createConsumerConfig()));

        run();
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

    private Properties createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", elasticSearchGroupId);
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
            executor.submit(new ElasticSearchPostConsumerThread(stream, serializationService, consumer, elasticSearch));
        }
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setElasticSearchGroupId(String elasticSearchGroupId) {
        this.elasticSearchGroupId = elasticSearchGroupId;
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
