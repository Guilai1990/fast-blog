package org.wkh.fastblog.domain;

import java.util.Date;
import java.util.UUID;

public class PostHelper {
    public static String generateTitleSlug(String title) {
        final String titleLower = title.toLowerCase().trim();

        /* TODO find a solution for non-Latin alphabets. I'm OK doing this because I don't expect other people to be
         * using this. Of course I'd solve this here and now if I were going to charge money for this and stuff.
         * I'll revisit it basically as soon as I hear that anyone besides me (WKH) is using this.
         *
         * So yes, I realize this is basically fucked for non-English speakers.
         */

        final String simplified = titleLower.replaceAll("[^a-zA-Z0-9 ]", "");
        final String excessSpaceRemoved = simplified.replaceAll("[ ]{2,}", " ");
        return excessSpaceRemoved.replaceAll(" ", "-");
    }

    public static String generateSlug(long offset, String slug) {
        if (offset != 0) {
            return offset + slug;
        } else {
            return null;
        }
    }

    public static Post fromForm(String title, String body, String summary) {
        String id = UUID.randomUUID().toString();
        String slug = generateTitleSlug(title);

        Post postRecord = Post.newBuilder()
                .setBody(body)
                .setTitle(title)
                .setSummary(summary)
                .setUpdatedAt(new Date().getTime())
                .setPublishedAt(new Date().getTime())
                .setCreatedAt(new Date().getTime())
                .setInitialOffset(0)
                .setPublished(false)
                .setId(id)
                .setTitleSlug(slug)
                .setSlug(slug)
                .setSoftDeleted(false)
                .build();

        return postRecord;
    }

}
