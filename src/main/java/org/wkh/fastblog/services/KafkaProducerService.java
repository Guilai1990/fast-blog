package org.wkh.fastblog.services;

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
    private String brokerList;

    private Producer<CharSequence, byte[]> producer;

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setProducerReset(String producerReset) {
        this.producerReset = producerReset;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties props = new Properties();

        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("auto.offset.reset", producerReset);
        props.put("bootstrap.servers", brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");

        producer = new KafkaProducer<CharSequence, byte[]>(props);
    }

    public Future<RecordMetadata> sendRecord(ProducerRecord<CharSequence, byte[]> record) {
        return producer.send(record);
    }
}
