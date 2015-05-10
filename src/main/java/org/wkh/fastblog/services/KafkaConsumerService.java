package org.wkh.fastblog.services;

import io.confluent.kafka.serializers.KafkaAvroDecoder;
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

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
    private String autoCommit;

    @NotNull
    private String consumerReset;

    @NotNull
    private String schemaRegistry;

    @NotNull
    private Integer threadCount;

    private ConsumerConnector consumer;

    private ExecutorService executor;

    @NotNull
    private String postsTopic;

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
        props.put("auto.commit.enable", autoCommit);
        props.put("auto.offset.reset", consumerReset);
        props.put("schema.registry.url", schemaRegistry);

        return props;
    }

    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(postsTopic, threadCount);

        Properties props = createConsumerConfig();
        VerifiableProperties vProps = new VerifiableProperties(props);

        // Create decoders for key and value
        KafkaAvroDecoder avroDecoder = new KafkaAvroDecoder(vProps);

        Map<String, List<KafkaStream<Object, Object>>> consumerMap =
                consumer.createMessageStreams(topicCountMap, avroDecoder, avroDecoder);
        List<KafkaStream<Object, Object>> streams = consumerMap.get(postsTopic);

        // Launch all the threads
        executor = Executors.newFixedThreadPool(threadCount);

        // Create ConsumerLogic objects and bind them to threads
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerLogic(stream, threadNumber));
            threadNumber++;
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

    public void setAutoCommit(String autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setConsumerReset(String consumerReset) {
        this.consumerReset = consumerReset;
    }

    public void setSchemaRegistry(String schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    public void setPostsTopic(String postsTopic) {
        this.postsTopic = postsTopic;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }
}
