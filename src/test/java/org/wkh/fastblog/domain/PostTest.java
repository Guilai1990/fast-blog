package org.wkh.fastblog.domain;

import org.apache.avro.generic.GenericRecord;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wkh.fastblog.WebApplication;
import org.wkh.fastblog.services.PostSchemaService;
import org.wkh.fastblog.services.SerializationService;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebIntegrationTest
public class PostTest {
    @Autowired
    private SerializationService serializationService;

    @Test
    public void testSlugGeneration() throws Exception {
        Long now = new Date().getTime();
        DateTimeUtils.setCurrentMillisFixed(now);

        Post post = new Post("   Post Title! #1   Part      2 ", "Body", "Summary");

        assertEquals(now + "-post-title-1-part-2", post.getSlug());
    }

    @Test
    public void testSerialization() throws Exception {
        Post post = new Post("title", "body", "summary");

        byte[] bytes = serializationService.serializePost(post);

        Post newPost = serializationService.deserializePost(bytes);

        assertEquals(newPost, post);
    }
}
