package org.wkh.fastblog.elasticsearch;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wkh.fastblog.domain.Post;
import org.wkh.fastblog.serialization.SerializationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Service
public class ElasticSearchService implements InitializingBean, DisposableBean {
    private Logger log = LoggerFactory.getLogger(ElasticSearchService.class);

    private Node node;
    private Client client;
    private IndexRequestBuilder indexRequestBuilder;

    @Autowired
    private SerializationService serializationService;

    @Override
    public void destroy() throws Exception {
        node.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        node = nodeBuilder().node();
        client = node.client();

        indexRequestBuilder = client.prepareIndex("fastblog", "post");
        indexRequestBuilder.setRefresh(true);
    }

    public IndexResponse indexPost(Post post) {
        Map<String, Object> record = serializationService.toMap(post);

        indexRequestBuilder.setId(post.getId().toString());

        return indexRequestBuilder
                .setSource(record)
                .execute()
                .actionGet();
    }

    public List<Post> searchForString(String query) {
        if (query == null) {
            query = "";
        }

        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query,
                "title", "body", "summary");

        SearchResponse searchResponse = client
                .prepareSearch("fastblog")
                .setQuery(queryBuilder)
                .execute()
                .actionGet();

        SearchHits hits = searchResponse.getHits();

        List<Post> posts = new ArrayList<Post>();

        Post.Builder builder = Post.newBuilder();

        for(SearchHit hit : hits.getHits()) {
            Map<String, Object> values = hit.getSource();

            Post post = builder.setBody((CharSequence) values.get("body"))
                    .setTitle((CharSequence) values.get("title"))
                    .setSummary((CharSequence) values.get("summary"))
                    .setCreatedAt((Long) values.get("created_at"))
                    .build();

            posts.add(post);
        }

        return posts;
    }
}