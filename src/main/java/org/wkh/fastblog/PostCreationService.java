package org.wkh.fastblog;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
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
    @NotNull
    private String postsTopic;

    private final String schemaPath = "post_schema.json";

    private final KafkaService kafkaService;
    private final Schema schema;

    @Autowired
    public PostCreationService(KafkaService kafkaService) throws Exception {
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

        ProducerRecord<String, GenericRecord> record = new ProducerRecord<String, GenericRecord>(
                postsTopic,
                id,
                postRecord
        );

        return kafkaService.sendRecord(record);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
}
