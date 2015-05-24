package org.wkh.fastblog.serialization;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;

import java.io.ByteArrayOutputStream;

@Service
public class SerializationService {
    public static final Schema POST_SCHEMA = Post.getClassSchema();

    public byte[] serializePost(Post post) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<GenericRecord>(POST_SCHEMA);

        GenericRecord postRecord = toRecord(post);

        writer.write(postRecord, encoder);
        encoder.flush();
        out.close();

        return out.toByteArray();
    }

    public Post deserializePost(byte[] bytes) throws Exception {
        SpecificDatumReader<GenericRecord> reader = new SpecificDatumReader<GenericRecord>(POST_SCHEMA);

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
        GenericRecord record = new GenericData.Record(POST_SCHEMA);

        record.put("id", post.getId());
        record.put("initial_offset", post.getInitialOffset());
        record.put("created_at", post.getCreatedAt());
        record.put("published", post.getPublished());
        record.put("published_at", post.getPublishedAt());
        record.put("updated_at", post.getUpdatedAt());
        record.put("title", post.getTitle());
        record.put("body", post.getBody());
        record.put("summary", post.getSummary());
        record.put("title_slug", post.getTitleSlug());
        record.put("slug", post.getSlug());
        record.put("soft_deleted", post.getSoftDeleted());

        return record;
    }

    public String getStringFromAvroObject(GenericRecord record, String field) {
        if(record.get(field) == null) {
            return null;
        }

        Utf8 id = (Utf8)record.get(field);
        return id.toString();
    }

    public Long getLongFromAvroObject(GenericRecord record, String field) {
        if(record.get(field) == null) {
            return null;
        }

        return (Long)record.get(field);
    }

    public Post fromRecord(GenericRecord record) {
        Post postRecord = new Post(
                getStringFromAvroObject(record, "id"),
                getLongFromAvroObject(record, "initial_offset"),
                getLongFromAvroObject(record, "created_at"),
                (Boolean) record.get("published"),
                getLongFromAvroObject(record, "published_at"),
                getLongFromAvroObject(record, "updated_at"),
                getStringFromAvroObject(record, "title"),
                getStringFromAvroObject(record, "body"),
                getStringFromAvroObject(record, "summary"),
                getStringFromAvroObject(record, "title_slug"),
                getStringFromAvroObject(record, "slug"),
                (Boolean) record.get("soft_deleted")
        );

        return postRecord;
    }
}
