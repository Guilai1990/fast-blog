package org.wkh.fastblog;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

@Component
@ConfigurationProperties(prefix="kafka.topics")
public class PostCreationService {
    @NotNull
    private static String postsTopic;

    public static final String schemaPath = "post_schema.json";

    private static final Producer<String, GenericRecord> producer = KafkaProducerSingleton.getProducer();
    private static final Schema.Parser parser = new Schema.Parser();
    private static Schema schema;

    static {
        try {
            InputStream schemaStream = PostCreationService.class.getClassLoader().getResourceAsStream(schemaPath);
            schema = parser.parse(schemaStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPostsTopic(String postsTopic) {
        PostCreationService.postsTopic = postsTopic;
    }

    public static Future<RecordMetadata> create(String body) {
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

        return producer.send(data);
    }
}
