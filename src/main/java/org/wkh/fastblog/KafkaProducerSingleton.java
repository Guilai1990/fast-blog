package org.wkh.fastblog;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix="kafka")
public class KafkaProducerSingleton implements InitializingBean {
    private final Logger log = LoggerFactory.getLogger(KafkaProducerSingleton.class);

    @NotNull
    private String zookeeper;

    @NotNull
    private String groupId;

    @NotNull
    private boolean autoCommit;

    @NotNull
    private String reset;

    @NotNull
    private String schemaRegistry;

    @NotNull
    private String brokerList;

    private Producer<String, GenericRecord> producer;

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public void setSchemaRegistry(String schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public Producer<String, GenericRecord> getProducer() {
       return producer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties props = new Properties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);

        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", autoCommit);
        props.put("auto.offset.reset", reset);
        props.put("schema.registry.url", schemaRegistry);
        props.put("bootstrap.servers", brokerList);

        producer = new KafkaProducer<String, GenericRecord>(props);
    }
}
