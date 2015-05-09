package org.wkh.fastblog.services;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.wkh.fastblog.domain.Post;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Future;

@Component
@ConfigurationProperties(prefix="kafka.topics")
public class PostCreationService {
    @NotNull
    private String postsTopic;
    private final KafkaService kafkaService;
    private final PostSchemaService postSchemaService;

    @Autowired
    public PostCreationService(KafkaService kafkaService, PostSchemaService postSchemaService) throws Exception {
        this.kafkaService = kafkaService;
        this.postSchemaService = postSchemaService;

    }

    public void setPostsTopic(String postsTopic) {
        this.postsTopic = postsTopic;
    }

    public Future<RecordMetadata> create(Post post) {
        GenericRecord postRecord = post.toRecord(postSchemaService.getSchema());

        ProducerRecord<String, GenericRecord> kafkaRecord = new ProducerRecord<String, GenericRecord>(
                postsTopic,
                post.getId(),
                postRecord
        );

        return kafkaService.sendRecord(kafkaRecord);
    }
}
