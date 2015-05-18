package org.wkh.fastblog.pg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;
import org.wkh.fastblog.domain.PostRecord;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
@ConfigurationProperties(value = "pg", ignoreUnknownFields = false)
public class PostDAO implements InitializingBean {
    private Logger log = LoggerFactory.getLogger(PostDAO.class);

    @NotNull
    private boolean dropOnStartup;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dropOnStartup) {
            jdbcTemplate.execute("drop table if exists posts");
            log.info("dropped posts table");
        }

        jdbcTemplate.execute("create table if not exists posts(" +
                "id text primary key," +
                "title text, " +
                "slug text, " +
                "body text, " +
                "summary text, " +
                "published boolean, " +
                "published_at timestamp, " +
                "created_at timestamp)");

        log.info("created posts table");
    }

    public void setDropOnStartup(boolean dropOnStartup) {
        this.dropOnStartup = dropOnStartup;
    }

    public List<Post> fetchAll() {
        return jdbcTemplate.query("select * from posts order by created_at desc", new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Post(
                        rs.getString("id"),
                        rs.getDate("created_at"),
                        rs.getBoolean("published"),
                        rs.getDate("published_at"),
                        rs.getString("title"),
                        rs.getString("body"),
                        rs.getString("summary"),
                        rs.getString("slug")
                );
            }
        });
    }
    public void insertRecord(PostRecord record) {
        Date publishedAt = null;

        if (record.getPublishedAt() != null) {
            publishedAt = new Date(record.getPublishedAt());
        }

        Date createdAt = null;

        if (record.getCreatedAt() != null) {
            createdAt = new Date(record.getCreatedAt());
        }

        try {
            jdbcTemplate.update("insert into posts(id, title, slug, body, summary, published, published_at, created_at)" +
                            "values(?,?,?,?,?,?,?,?)",
                    record.getId(), record.getTitle(), record.getSlug(), record.getBody(), record.getSummary(),
                    record.getPublished(), publishedAt, createdAt);
        } catch (Exception e) {
           log.error(e.toString());
        }

    }
}
