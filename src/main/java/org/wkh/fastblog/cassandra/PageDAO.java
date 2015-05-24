package org.wkh.fastblog.cassandra;

import com.datastax.driver.core.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Page;

@Service
public class PageDAO implements InitializingBean {
    @Autowired
    private CassandraService cassandra;
    private Session session;

    private final String insertPageQuery = "INSERT INTO fastblog.pages (id, body) VALUES(?, ?)";
    private PreparedStatement insertPageStatement;

    private final String fetchPageQuery = "SELECT * FROM fastblog.pages WHERE id = ?";
    private PreparedStatement fetchPageStatement;

    @Override
    public void afterPropertiesSet() throws Exception {
        session = cassandra.getSession();

        insertPageStatement = session.prepare(insertPageQuery);
        fetchPageStatement = session.prepare(fetchPageQuery);
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
