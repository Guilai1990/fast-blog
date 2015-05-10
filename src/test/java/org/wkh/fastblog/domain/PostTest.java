package org.wkh.fastblog.domain;

import org.apache.avro.generic.GenericRecord;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.wkh.fastblog.services.PostSchemaService;

import java.util.Date;

import static org.junit.Assert.*;

public class PostTest {
    @Test
    public void testSerialization() throws Exception {
        Long now = new Date().getTime();
        DateTimeUtils.setCurrentMillisFixed(now);

        PostSchemaService postSchemaService = new PostSchemaService();
        Post post = new Post("   Post Title! #1   Part      2 ", "Body", "Summary");

        GenericRecord record = post.toRecord(postSchemaService.getSchema());

        assertEquals(post.getId(), record.get("id"));
        assertEquals(now + "-post-title-1-part-2", record.get("slug"));

        Post reconstructedPost = Post.fromRecord(record);

        assertEquals(reconstructedPost, post);
    }
}
