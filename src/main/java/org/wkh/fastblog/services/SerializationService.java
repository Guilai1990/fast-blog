package org.wkh.fastblog.services;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;

import java.io.ByteArrayOutputStream;
import java.util.Date;

@Service
public class SerializationService {
    private Logger log = LoggerFactory.getLogger(SerializationService.class);

    @Autowired
    private PostSchemaService postSchemaService;

    public byte[] serializePost(Post post) throws Exception {
        Schema postSchema = postSchemaService.getSchema();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<GenericRecord>(postSchema);

        GenericRecord postRecord = toRecord(post);

        writer.write(postRecord, encoder);
        encoder.flush();
        out.close();
        return out.toByteArray();
    }

    public Post deserializePost(byte[] bytes) throws Exception {
        SpecificDatumReader<GenericRecord> reader = new SpecificDatumReader<GenericRecord>(postSchemaService.getSchema());

        Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        GenericRecord record = reader.read(null, decoder);

        return fromRecord(record);
    }

    /**
     * Serialize this post to an Avro record.
     *
     * @return GenericRecord
     */
    public GenericRecord toRecord(Post post) {
        GenericRecord record = new GenericData.Record(postSchemaService.getSchema());

        record.put("id", post.getId());
        record.put("created_at", post.getCreatedAt().getTime());
        record.put("published", post.isPublished());

        if (post.getPublishedAt() != null) {
            record.put("published_at", post.getPublishedAt().getTime());
        } else {
            record.put("published_at", null);
        }

        record.put("title", post.getTitle());
        record.put("body", post.getBody());
        record.put("summary", post.getSummary());
        record.put("slug", post.getSlug());

        return record;
    }

    public String getStringFromAvroObject(GenericRecord record, String field) {
        Utf8 id = (Utf8)record.get(field);
        return id.toString();
    }

    public Post fromRecord(GenericRecord record) {
        log.info("Trying to serialize from record with ID: " + record.get("id"));

        Date createdAt = new Date((Long)record.get("created_at"));

        Date publishedAt = null;

        if (record.get("published_at") != null) {
            publishedAt = new Date((Long)record.get("published_at"));
        }

        Post post = new Post(
                getStringFromAvroObject(record, "id"),
                createdAt,
                (Boolean) record.get("published"),
                publishedAt,
                getStringFromAvroObject(record, "title"),
                getStringFromAvroObject(record, "body"),
                getStringFromAvroObject(record, "summary"),
                getStringFromAvroObject(record, "slug")
        );

        log.info("Got to the end");

        return post;
    }
}
