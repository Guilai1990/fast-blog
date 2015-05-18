package org.wkh.fastblog.cassandra;

import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;
import org.wkh.fastblog.domain.PostRecord;

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

    @NotNull
    private boolean dropOnStartup;

    private Cluster cluster;
    private Session session;

    private final String insertPostQuery = "INSERT INTO fastblog.posts (id, title, slug, body, summary, published, published_at, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private PreparedStatement insertPostStatement;

    private final String fetchPostsQuery = "SELECT * FROM fastblog.posts";
    private BoundStatement fetchPostsStatement;

    public void setHost(String host) {
        this.host = host;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public void setDropOnStartup(boolean dropOnStartup) {
        this.dropOnStartup = dropOnStartup;
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
        fetchPostsStatement = session.prepare(fetchPostsQuery).bind();
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

        final String dropPostTableQuery = "DROP TABLE IF EXISTS fastblog.posts;";

        final String postTableCreationQuery = "CREATE TABLE IF NOT EXISTS fastblog.posts (" +
                "id text primary key," +
                "title text, " +
                "slug text, " +
                "body text, " +
                "summary text, " +
                "published boolean, " +
                "published_at timestamp, " +
                "created_at timestamp);";

        log.info("Creating keyspace with query: ");
        log.info(keyspaceCreationQuery);

        session.execute(keyspaceCreationQuery);

        if (dropOnStartup) {
            session.execute(dropPostTableQuery);
            log.info("Dropped posts table");
        }

        log.info("Creating posts table with query: ");
        log.info(postTableCreationQuery);

        session.execute(postTableCreationQuery);

        session.execute("CREATE INDEX IF NOT EXISTS posts_published ON fastblog.posts (published);");
    }

    public void insert(PostRecord postRecord) {
        Date publishedAt = null;
        
        if (postRecord.getPublishedAt() != null) {
            publishedAt = new Date(postRecord.getPublishedAt());
        }

        Date createdAt = null;

        if (postRecord.getCreatedAt() != null) {
            createdAt = new Date(postRecord.getCreatedAt());
        }

        try {
            /* (id, title, slug, body, summary, published, published_at, created_at) */

            BoundStatement bound = insertPostStatement.bind(
                    postRecord.getId(),
                    postRecord.getTitle(),
                    postRecord.getSlug(),
                    postRecord.getBody(),
                    postRecord.getSummary(),
                    postRecord.getPublished(),
                    publishedAt,
                    createdAt);

            session.execute(bound);
        } catch(Exception e) {
            log.error(e.toString());
        }

    }

    public List<Post> fetchAll() {
        return fetchAllByQuery(fetchPostsStatement);
    }

    private List<Post> fetchAllByQuery(BoundStatement query) {
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
                        row.getDate("created_at"),
                        row.getBool("published"),
                        row.getDate("published_at"),
                        row.getString("title"),
                        row.getString("body"),
                        row.getString("summary"),
                        row.getString("slug")
                );
    }
}