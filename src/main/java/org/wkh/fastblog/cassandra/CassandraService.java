package org.wkh.fastblog.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@ConfigurationProperties(prefix="cassandra")
public class CassandraService implements InitializingBean, DisposableBean {
    private Logger log = LoggerFactory.getLogger(CassandraService.class);
    @NotNull
    private String host;

    @NotNull
    private int replicationFactor;

    @NotNull
    private boolean dropOnStartup;

    private Cluster cluster;

    private Session session;

    public void setHost(String host) {
        this.host = host;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public void setDropOnStartup(boolean dropOnStartup) {
        this.dropOnStartup = dropOnStartup;
    }

    public Session getSession() {
        return session;
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

    @Override
    public void destroy() throws Exception {
        session.close();
        cluster.close();
        log.info("Cassandra cluster connection closed");
    }

}
