package org.wkh.fastblog.cassandra;

import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Page;
import org.wkh.fastblog.domain.Post;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@ConfigurationProperties(prefix="cassandra")
public class PostDAO implements InitializingBean {
    private Logger log = LoggerFactory.getLogger(PostDAO.class);

    @NotNull
    private Long partition;

    @Autowired
    private CassandraService cassandra;

    private Session session;

    private final String insertPostQuery = "INSERT INTO fastblog.posts (partition, initial_offset, id, title, title_slug, slug, body, summary, published, published_at, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement insertPostStatement;

    private final String fetchPostsQuery = "SELECT * FROM fastblog.posts";
    private BoundStatement fetchPostsStatement;

    public void setPartition(Long partition) {
        this.partition = partition;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        session = cassandra.getSession();

        insertPostStatement = session.prepare(insertPostQuery);
        fetchPostsStatement = session.prepare(fetchPostsQuery).bind();

    }

    public Date longToDate(Long offset) {
        if(offset != null) {
            return new Date(offset);
        } else {
            return null;
        }
    }

    public void insertRecord(Post postRecord) {
        Date publishedAt = longToDate(postRecord.getPublishedAt());
        Date createdAt = longToDate(postRecord.getCreatedAt());
        Date updatedAt = longToDate(postRecord.getUpdatedAt());

        try {
            /* (partition, initial_offset, id, title, title_slug, slug, body, summary, published, published_at, created_at, updated_at) */

            BoundStatement bound = insertPostStatement.bind(
                    partition,
                    postRecord.getInitialOffset(),
                    postRecord.getId(),
                    postRecord.getTitle(),
                    postRecord.getTitleSlug(),
                    postRecord.getSlug(),
                    postRecord.getBody(),
                    postRecord.getSummary(),
                    postRecord.getPublished(),
                    publishedAt,
                    createdAt,
                    updatedAt);

            session.execute(bound);
        } catch(Exception e) {
            log.error(e.toString());
        }

    }

    public List<Post> fetchAllPosts() {
        return fetchAllPostsByQuery(fetchPostsStatement);
    }

    private List<Post> fetchAllPostsByQuery(BoundStatement query) {
        ResultSet results = session.execute(query);

        List<Post> posts = new ArrayList<Post>();

        for(Row row : results.all()) {
            Post post = serializePost(row);
            posts.add(post);
        }

        return posts;
    }

    private Post serializePost(Row row) {
        return new Post(
                row.getString("id"),
                row.getLong("initial_offset"),
                row.getDate("created_at").getTime(),
                row.getBool("published"),
                row.getDate("published_at").getTime(),
                row.getDate("updated_at").getTime(),
                row.getString("title"),
                row.getString("body"),
                row.getString("summary"),
                row.getString("title_slug"),
                row.getString("slug"),
                false /* soft_deleted */
        );
    }
}
