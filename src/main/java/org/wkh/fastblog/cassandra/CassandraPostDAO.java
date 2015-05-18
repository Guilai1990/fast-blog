package org.wkh.fastblog.cassandra;

import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@ConfigurationProperties(prefix="cassandra")
public class CassandraPostDAO implements InitializingBean, DisposableBean {
    private Logger log = LoggerFactory.getLogger(CassandraPostDAO.class);

    @NotNull
    private String host;

    @NotNull
    private int replicationFactor;

    private Cluster cluster;
    private Session session;

    private String insertPostQuery = "INSERT INTO fastblog.posts (id, title, slug, body, summary, published, published_at, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private PreparedStatement insertPostStatement;

    private String fetchPostsQuery = "SELECT * FROM fastblog.posts";

    public void setHost(String host) {
        this.host = host;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cluster = Cluster.builder()
                .addContactPoint(host)
                .build();

        session = cluster.connect();
        log.info("Connected to Cassandra cluster");

        createSchema();

        log.info("Created/updated schema");

        insertPostStatement = session.prepare(insertPostQuery);
    }

    @Override
    public void destroy() throws Exception {
        session.close();
        cluster.close();
        log.info("Cassandra cluster connection closed");
    }

    public void createSchema() {
        final String keyspaceCreationQuery = "CREATE KEYSPACE IF NOT EXISTS fastblog WITH replication " +
                "= {'class':'SimpleStrategy', 'replication_factor':1};";


        final String postTableCreationQuery = "CREATE TABLE IF NOT EXISTS fastblog.posts (" +
                "id text," +
                "title text, " +
                "slug text, " +
                "body text, " +
                "summary text, " +
                "published boolean, " +
                "published_at timestamp, " +
                "created_at timestamp, " +
                "PRIMARY KEY (id, created_at)" +
                ") WITH CLUSTERING ORDER BY (created_at DESC);";

        log.info("Creating keyspace with query: ");
        log.info(keyspaceCreationQuery);

        session.execute(keyspaceCreationQuery);

        log.info("Creating posts table with query: ");
        log.info(postTableCreationQuery);

        session.execute(postTableCreationQuery);

        session.execute("CREATE INDEX IF NOT EXISTS posts_published ON fastblog.posts (published);");
        session.execute("CREATE INDEX IF NOT EXISTS posts_published_at ON fastblog.posts (published_at);");
        session.execute("CREATE INDEX IF NOT EXISTS posts_created_at ON fastblog.posts (created_at);");
    }

    public void insert(Post post) {
        Date publishedAt = null;
        
        if (post.getPublishedAt() != null) {
            publishedAt = new Date(post.getPublishedAt());
        }

        Date createdAt = null;

        if (post.getCreatedAt() != null) {
            createdAt = new Date(post.getCreatedAt());
        }

        try {
            /* (id, title, slug, body, summary, published, published_at, created_at) */

            BoundStatement bound = insertPostStatement.bind(
                    post.getId(),
                    post.getTitle(),
                    post.getSlug(),
                    post.getBody(),
                    post.getSummary(),
                    post.getPublished(),
                    publishedAt,
                    createdAt);

            session.execute(bound);
        } catch(Exception e) {
            log.error(e.toString());
        }

    }

    public List<Post> fetchAll() {
        ResultSet results = session.execute(fetchPostsQuery);

        List<Post> posts = new ArrayList<Post>();

        for(Row row : results.all()) {
            Long publishedAt = null;

            if (row.getDate("published_at") != null) {
                publishedAt = row.getDate("published_at").getTime();
            }

            Post post = new Post(
                    row.getString("id"),
                    row.getDate("created_at").getTime(),
                    row.getBool("published"),
                    publishedAt,
                    row.getString("title"),
                    row.getString("body"),
                    row.getString("summary"),
                    row.getString("slug")
            );

            posts.add(post);
        }
        return posts;
    }
}
