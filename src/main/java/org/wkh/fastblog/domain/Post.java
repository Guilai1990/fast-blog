package org.wkh.fastblog.domain;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Post implements Serializable {
    private static Logger log = LoggerFactory.getLogger(Post.class);

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

    public Post(String id, Date createdAt, boolean published, Date publishedAt, String title, String body,
                String summary, String slug) {
        this.id = id;
        this.createdAt = createdAt;
        this.published = published;
        this.publishedAt = publishedAt;
        this.title = title;
        this.body = body;
        this.summary = summary;
        this.slug = slug;
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


    public String getSlug() {
        return slug;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isPublished() {
        return published;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getSummary() {
        return summary;
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

    public static String getStringFromAvroObject(GenericRecord record, String field) {
        Utf8 id = (Utf8)record.get(field);
        return id.toString();
    }

    public static Post fromRecord(GenericRecord record) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (isPublished() != post.isPublished()) return false;
        if (!getId().equals(post.getId())) return false;
        if (!getCreatedAt().equals(post.getCreatedAt())) return false;
        if (getPublishedAt() != null ? !getPublishedAt().equals(post.getPublishedAt()) : post.getPublishedAt() != null)
            return false;
        if (!getTitle().equals(post.getTitle())) return false;
        if (!getBody().equals(post.getBody())) return false;
        if (!getSummary().equals(post.getSummary())) return false;
        return getSlug().equals(post.getSlug());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCreatedAt().hashCode();
        result = 31 * result + (isPublished() ? 1 : 0);
        result = 31 * result + (getPublishedAt() != null ? getPublishedAt().hashCode() : 0);
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getBody().hashCode();
        result = 31 * result + getSummary().hashCode();
        result = 31 * result + getSlug().hashCode();
        return result;
    }
}
