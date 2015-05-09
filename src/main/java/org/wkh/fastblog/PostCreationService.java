package org.wkh.fastblog;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

@Component
@ConfigurationProperties(prefix="kafka.topics")
public class PostCreationService implements InitializingBean {
    private final Logger log = LoggerFactory.getLogger(KafkaProducerSingleton.class);

    @NotNull
    private String postsTopic;

    private final String schemaPath = "post_schema.json";

    private final KafkaProducerSingleton kafkaService;
    private final Schema schema;

    @Autowired
    public PostCreationService(KafkaProducerSingleton kafkaService) throws Exception {
        this.kafkaService = kafkaService;

        final Schema.Parser parser = new Schema.Parser();
        InputStream schemaStream = PostCreationService.class.getClassLoader().getResourceAsStream(schemaPath);
        schema = parser.parse(schemaStream);
    }

    public void setPostsTopic(String postsTopic) {
        this.postsTopic = postsTopic;
    }

    public Future<RecordMetadata> create(String body) {
        String id = UUID.randomUUID().toString();

        GenericRecord postRecord = new GenericData.Record(schema);

        postRecord.put("id", id);
        postRecord.put("created_at", new Date().getTime());
        postRecord.put("title", "WIP placeholder");
        postRecord.put("body", body);
        postRecord.put("slug", "wip-placeholder");

        ProducerRecord<String, GenericRecord> data = new ProducerRecord<String, GenericRecord>(
                postsTopic,
                id,
                postRecord
        );

        Producer<String, GenericRecord> producer = kafkaService.getProducer();

        return producer.send(data);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
}
