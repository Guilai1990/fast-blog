package org.wkh.fastblog;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaProducerSingleton {
    private static Producer<String, GenericRecord> producer;

    /* TODO Springify this so it can be an injected bean with @ConfigurationProperties
    *
    * yes, I know this is bad. calm down. calm down. easy.
    *
    * The good news is that KafkaProducer *is* thread-safe, so we should be OK passing this instance
    * around to other threads.
    * */

    public static Producer<String, GenericRecord> getProducer() {
        if (producer != null) {
            return producer;
        }

        Properties props = new Properties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);

        /*
         * these shouldn't be hard-coded. they should be set in a .properties file
         * and read with a @ConfigurationProperties annotation
         */

        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "fast-blog");
        props.put("auto.commit.enable", "false");
        props.put("auto.offset.reset", "smallest");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("bootstrap.servers", "localhost:9092");

        producer = new KafkaProducer<String, GenericRecord>(props);

        return producer;
    }
}
