package org.wkh.fastblog;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix="kafka")
public class KafkaProducerSingleton {
    private final static Logger log = LoggerFactory.getLogger(KafkaProducerSingleton.class);

    @NotNull
    private static String zookeeper;

    @NotNull
    private static String groupId;

    @NotNull
    private static boolean autoCommit;

    @NotNull
    private static String reset;

    @NotNull
    private static String schemaRegistry;

    @NotNull
    private static String brokerList;

    private static Producer<String, GenericRecord> producer;

    /* TODO Springify this so it can be an injected bean with @ConfigurationProperties
    *
    * yes, I know this is bad. calm down. calm down. easy.
    *
    * The good news is that KafkaProducer *is* thread-safe, so we should be OK passing this instance
    * around to other threads.
    * */

    public static void setZookeeper(String zookeeper) {
        KafkaProducerSingleton.zookeeper = zookeeper;
    }

    public static void setGroupId(String groupId) {
        KafkaProducerSingleton.groupId = groupId;
    }

    public static void setAutoCommit(boolean autoCommit) {
        KafkaProducerSingleton.autoCommit = autoCommit;
    }

    public static void setReset(String reset) {
        KafkaProducerSingleton.reset = reset;
    }

    public static void setSchemaRegistry(String schemaRegistry) {
        KafkaProducerSingleton.schemaRegistry = schemaRegistry;
    }

    public static void setBrokerList(String brokerList) {
        KafkaProducerSingleton.brokerList = brokerList;
    }

    public static Producer<String, GenericRecord> getProducer() {
        if (producer != null) {
            return producer;
        }

        Properties props = new Properties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);

        log.info("Zookeeper URL: " + zookeeper);
        log.info("Group ID: " + groupId);
        log.info("Autocommit: " + autoCommit);
        log.info("Reset: " + reset);
        log.info("Schema registry URL: " + schemaRegistry);
        log.info("Brokers: " + brokerList);

        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", autoCommit);
        props.put("auto.offset.reset", reset);
        props.put("schema.registry.url", schemaRegistry);
        props.put("bootstrap.servers", brokerList);

        producer = new KafkaProducer<String, GenericRecord>(props);

        return producer;
    }
}
