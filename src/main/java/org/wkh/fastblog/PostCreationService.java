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
import java.util.UUID;
import java.util.concurrent.Future;

@Component
@ConfigurationProperties(prefix="kafka.topics")
public class PostCreationService {
    @NotNull
    private static String postsTopic;

    public static final String schemaString = "{\"namespace\": \"org.wkh.fastblog\", \"type\": \"record\", " +
            "\"name\": \"post\"," +
            "\"fields\": [" +
            "{\"name\": \"id\", \"type\": \"string\"}," +
            "{\"name\": \"body\", \"type\": \"string\"}" +
            "]}";

    private static final Producer<String, GenericRecord> producer = KafkaProducerSingleton.getProducer();
    private static final Schema.Parser parser = new Schema.Parser();
    private static final Schema schema = parser.parse(schemaString);

    public static void setPostsTopic(String postsTopic) {
        PostCreationService.postsTopic = postsTopic;
    }

    public static Future<RecordMetadata> create(String body) {
        String id = UUID.randomUUID().toString();

        GenericRecord postRecord = new GenericData.Record(schema);
        postRecord.put("id", id);
        postRecord.put("body", body);

        ProducerRecord<String, GenericRecord> data = new ProducerRecord<String, GenericRecord>(
                postsTopic,
                id,
                postRecord
        );

        return producer.send(data);
    }
}
