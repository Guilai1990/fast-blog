package org.wkh.fastblog.services;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Properties;
import java.util.concurrent.Future;

@Service
@ConfigurationProperties(prefix="kafka")
public class KafkaProducerService implements InitializingBean {
    @NotNull
    private String zookeeper;

    @NotNull
    private String groupId;

    @NotNull
    private String producerReset;

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

    public void setProducerReset(String producerReset) {
        this.producerReset = producerReset;
    }

    public void setSchemaRegistry(String schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
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
        props.put("auto.offset.reset", producerReset);
        props.put("schema.registry.url", schemaRegistry);
        props.put("bootstrap.servers", brokerList);

        producer = new KafkaProducer<String, GenericRecord>(props);
    }

    public Future<RecordMetadata> sendRecord(ProducerRecord<String, GenericRecord> record) {
        return producer.send(record);
    }
}