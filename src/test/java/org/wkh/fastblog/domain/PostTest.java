package org.wkh.fastblog.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wkh.fastblog.WebApplication;
import org.wkh.fastblog.serialization.SerializationService;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebIntegrationTest
public class PostTest {
    @Autowired
    private SerializationService serializationService;

    @Test
    public void testTitleSlugGeneration() throws Exception {
        String title = "   Post Title! #1   Part      2 ";

        assertEquals("post-title-1-part-2", PostHelper.generateTitleSlug(title));
    }

    @Test
    public void testSerialization() throws Exception {
        Post postRecord = PostHelper.fromForm("title", "body", "summary");

        byte[] bytes = serializationService.serializePost(postRecord);

        Post newPost = serializationService.deserializePost(bytes);

        assertEquals(newPost, postRecord);
    }
}
