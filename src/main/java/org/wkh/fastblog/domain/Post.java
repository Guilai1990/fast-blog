package org.wkh.fastblog.domain;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.joda.time.DateTimeUtils;

import java.util.Date;
import java.util.UUID;

public class Post {
    private final String id;
    private final Date createdAt;
    private boolean published;
    private Date publishedAt;
    private final String title;
    private final String body;
    private final String summary;
    private final String slug;

    public Post(String title, String body, String summary) {
        createdAt = new Date();
        published = false;
        publishedAt = null;

        this.id = UUID.randomUUID().toString();

        this.title = title;
        this.body = body;
        this.summary = summary;

        this.slug = generateSlug(title);
    }

    private String generateSlug(String title) {
        final String titleLower = title.toLowerCase().trim();

        /* TODO find a solution for non-Latin alphabets. I'm OK doing this because I don't expect other people to be
         * using this. Of course I'd solve this here and now if I were going to charge money for this and stuff.
         * I'll revisit it basically as soon as I hear that anyone besides me (WKH) is using this.
         *
         * So yes, I realize this is basically fucked for non-English speakers.
         */

        final String simplified = titleLower.replaceAll("[^a-zA-Z0-9 ]", "");

        final Long now = DateTimeUtils.currentTimeMillis();

        /* Two posts created at precisely the same time (or around the same time on other systems due to clock drift)
         * could possibly create identical slugs. This is intended to be a single user creating posts so it's not an
         * issue in practice.
         */

        final String excessSpaceRemoved = simplified.replaceAll("[ ]{2,}", " ");
        final String snakeCase = excessSpaceRemoved.replaceAll(" ", "-");

        return now + "-" + snakeCase;
    }

    public String getId() {
        return id;
    }

    /**
     * Serialize this post to an Avro record.
     *
     * @return GenericRecord
     */
    public GenericRecord toRecord(Schema schema) {
        GenericRecord record = new GenericData.Record(schema);

        record.put("id", id);
        record.put("created_at", createdAt.getTime());
        record.put("published", published);

        if (publishedAt != null) {
            record.put("published_at", publishedAt.getTime());
        } else {
            record.put("published_at", null);
        }

        record.put("title", title);
        record.put("body", body);
        record.put("summary", summary);
        record.put("slug", slug);

        return record;
    }
}
