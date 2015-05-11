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
@ConfigurationProperties(prefix="kafka")
public class PostCreationService {
    public static String postListKey = "posts";

    @NotNull
    private String postsTopic;
    private final KafkaProducerService kafkaProducerService;
    private final PostSchemaService postSchemaService;
    private final SerializationService serializationService;

    @Autowired
    public PostCreationService(KafkaProducerService kafkaProducerService, PostSchemaService postSchemaService, SerializationService serializationService) throws Exception {
        this.kafkaProducerService = kafkaProducerService;
        this.postSchemaService = postSchemaService;
        this.serializationService = serializationService;
    }

    public void setPostsTopic(String postsTopic) {
        this.postsTopic = postsTopic;
    }

    public Future<RecordMetadata> create(Post post) {
        GenericRecord postRecord = serializationService.toRecord(post);

        ProducerRecord<String, GenericRecord> kafkaRecord = new ProducerRecord<String, GenericRecord>(
                postsTopic,
                post.getId(),
                postRecord
        );

        return kafkaProducerService.sendRecord(kafkaRecord);
    }
}
