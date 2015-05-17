package org.wkh.fastblog.domain;

import org.joda.time.DateTimeUtils;

import java.util.Date;
import java.util.UUID;

public class PostForm {
    public static String generateSlug(String title) {
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

    public static Post fromForm(String title, String body, String summary) {
        String id = UUID.randomUUID().toString();
        String slug = generateSlug(title);

        Post post = Post.newBuilder()
                .setBody(body)
                .setTitle(title)
                .setSummary(summary)
                .setCreatedAt(new Date().getTime())
                .setPublished(false)
                .setPublishedAt(null)
                .setId(id)
                .setSlug(slug)
                .build();

        return post;
    }
}
