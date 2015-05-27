package org.wkh.fastblog.kafka;

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
    private String brokerList;

    private Producer<byte[], byte[]> producer;

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties props = new Properties();

        props.put("zookeeper.connect", zookeeper);
        props.put("bootstrap.servers", brokerList);
        props.put("serializer.class", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("request.required.acks", "1");

        producer = new KafkaProducer<byte[], byte[]>(props);
    }

    public Future<RecordMetadata> sendRecord(ProducerRecord<byte[], byte[]> record) {
        return producer.send(record);
    }
}
