package org.wkh.fastblog.domain;

import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wkh.fastblog.WebApplication;
import org.wkh.fastblog.serialization.SerializationService;

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

        String title = "   Post Title! #1   Part      2 ";

        assertEquals(now + "-post-title-1-part-2", Post.generateSlug(title));
    }

    @Test
    public void testSerialization() throws Exception {
        PostRecord postRecord = Post.fromForm("title", "body", "summary");

        byte[] bytes = serializationService.serializePost(postRecord);

        PostRecord newPostRecord = serializationService.deserializePost(bytes);

        assertEquals(newPostRecord, postRecord);
    }
}