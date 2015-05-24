package org.wkh.fastblog.cassandra;

import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
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
public class CassandraDAO implements InitializingBean, DisposableBean {
    private Logger log = LoggerFactory.getLogger(CassandraDAO.class);

    @NotNull
    private String host;

    @NotNull
    private int replicationFactor;

    @NotNull
    private boolean dropOnStartup;

    @NotNull
    private Long partition;

    private Cluster cluster;
    private Session session;

    private final String insertPostQuery = "INSERT INTO fastblog.posts (partition, initial_offset, id, title, title_slug, slug, body, summary, published, published_at, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement insertPostStatement;

    private final String fetchPostsQuery = "SELECT * FROM fastblog.posts";
    private BoundStatement fetchPostsStatement;

    private final String insertPageQuery = "INSERT INTO fastblog.pages (id, body) VALUES(?, ?)";
    private PreparedStatement insertPageStatement;

    private final String fetchPageQuery = "SELECT * FROM fastblog.pages WHERE id = ?";
    private PreparedStatement fetchPageStatement;

    public void setHost(String host) {
        this.host = host;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public void setDropOnStartup(boolean dropOnStartup) {
        this.dropOnStartup = dropOnStartup;
    }

    public void setPartition(Long partition) {
        this.partition = partition;
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

        insertPageStatement = session.prepare(insertPageQuery);
        fetchPageStatement = session.prepare(fetchPageQuery);
    }

    @Override
    public void destroy() throws Exception {
        session.close();
        cluster.close();
        log.info("Cassandra cluster connection closed");
    }

    public void createSchema() {
        final String keyspaceCreationQuery = "CREATE KEYSPACE IF NOT EXISTS fastblog WITH replication " +
                "= {'class':'SimpleStrategy', 'replication_factor':1}";

        final String postTableCreationQuery = "CREATE TABLE IF NOT EXISTS fastblog.posts (" +
                "partition bigint," +
                "initial_offset bigint," +
                "id text, " +
                "title text, " +
                "title_slug text, " +
                "slug text, " +
                "body text, " +
                "summary text, " +
                "published boolean, " +
                "published_at timestamp, " +
                "created_at timestamp, " +
                "updated_at timestamp, " +
                "primary key (partition, initial_offset)) with clustering order by (initial_offset desc)";

        log.info("Creating keyspace with query: ");
        log.info(keyspaceCreationQuery);

        session.execute(keyspaceCreationQuery);

        if (dropOnStartup) {
            session.execute("DROP TABLE IF EXISTS fastblog.posts");
            session.execute("DROP TABLE IF EXISTS fastblog.pages");
            log.info("Dropped posts table");
        }

        log.info("Creating posts table with query: ");
        log.info(postTableCreationQuery);

        session.execute(postTableCreationQuery);

        session.execute("CREATE INDEX IF NOT EXISTS posts_published ON fastblog.posts (published);");

        session.execute("CREATE TABLE IF NOT EXISTS fastblog.pages (" +
                "id text primary key," +
                "body text)");
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

    public void insertPage(Page page) {
        BoundStatement bound = insertPageStatement.bind(
                page.getId(),
                page.getBody()
        );

        session.execute(bound);
    }

    public String fetchPageBody(String id) {
        BoundStatement bound = fetchPageStatement.bind(id);

        final ResultSet resultSet = session.execute(bound);

        if(resultSet.isExhausted()) {
            return "";
        }

        Row result = resultSet.one();

        if(result.isNull("body")) {
            return "";
        }

        return result.getString("body");
    }
}
