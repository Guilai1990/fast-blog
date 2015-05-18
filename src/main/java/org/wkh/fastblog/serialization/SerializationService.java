package org.wkh.fastblog.serialization;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.PostRecord;

import java.io.ByteArrayOutputStream;

@Service
public class SerializationService {
    public static final Schema POST_SCHEMA = PostRecord.getClassSchema();

    public byte[] serializePost(PostRecord post) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<GenericRecord>(POST_SCHEMA);

        GenericRecord postRecord = toRecord(post);

        writer.write(postRecord, encoder);
        encoder.flush();
        out.close();

        return out.toByteArray();
    }

    public PostRecord deserializePost(byte[] bytes) throws Exception {
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
    public GenericRecord toRecord(PostRecord postRecord) {
        GenericRecord record = new GenericData.Record(POST_SCHEMA);

        record.put("id", postRecord.getId());
        record.put("created_at", postRecord.getCreatedAt());
        record.put("published", postRecord.getPublished());

        if (postRecord.getPublishedAt() != null) {
            record.put("published_at", postRecord.getPublishedAt());
        } else {
            record.put("published_at", null);
        }

        record.put("title", postRecord.getTitle());
        record.put("body", postRecord.getBody());
        record.put("summary", postRecord.getSummary());
        record.put("slug", postRecord.getSlug());
        record.put("stored_in_rdbms", postRecord.getStoredInRdbms());
        record.put("soft_deleted", postRecord.getSoftDeleted());

        return record;
    }

    public String getStringFromAvroObject(GenericRecord record, String field) {
        Utf8 id = (Utf8)record.get(field);
        return id.toString();
    }

    public PostRecord fromRecord(GenericRecord record) {
        Long createdAt = (Long)record.get("created_at");

        Long publishedAt = null;

        if (record.get("published_at") != null) {
            publishedAt = (Long)record.get("published_at");
        }

        PostRecord postRecord = new PostRecord(
                getStringFromAvroObject(record, "id"),
                createdAt,
                (Boolean) record.get("published"),
                publishedAt,
                getStringFromAvroObject(record, "title"),
                getStringFromAvroObject(record, "body"),
                getStringFromAvroObject(record, "summary"),
                getStringFromAvroObject(record, "slug"),
                (Boolean) record.get("stored_in_rdbms"),
                (Boolean) record.get("soft_deleted")
        );

        return postRecord;
    }
}
