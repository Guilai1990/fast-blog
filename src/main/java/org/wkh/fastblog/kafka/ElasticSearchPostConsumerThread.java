package org.wkh.fastblog.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.elasticsearch.action.index.IndexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wkh.fastblog.domain.Post;
import org.wkh.fastblog.domain.PostHelper;
import org.wkh.fastblog.elasticsearch.ElasticSearchService;
import org.wkh.fastblog.serialization.SerializationService;

public class ElasticSearchPostConsumerThread implements Runnable {
    private Logger log = LoggerFactory.getLogger(ElasticSearchPostConsumerThread.class);
    private KafkaStream stream;
    private ConsumerConnector consumerConnector;
    private SerializationService serializationService;
    private ElasticSearchService elasticSearch;

    public ElasticSearchPostConsumerThread(KafkaStream stream, SerializationService serializationService,
                                           ConsumerConnector consumerConnector, ElasticSearchService elasticSearch) {
        this.stream = stream;
        this.serializationService = serializationService;
        this.consumerConnector = consumerConnector;
        this.elasticSearch = elasticSearch;
    }

    @Override
    public void run() {
        log.info("Elasticsearch consumer thread started");

        ConsumerIterator<byte[], byte[]> it = stream.iterator();

        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> record = it.next();
            byte[] message = record.message();


            log.info("Going to deserialize post in Elasticsearch thread");

            Post post;

            try {
                post = serializationService.deserializePost(message);

                if(post.getInitialOffset() == 0) {
                    post.setInitialOffset(record.offset());
                }

                final String slug = PostHelper.generateSlug(record.offset(), (String) post.getTitleSlug());

                post.setSlug(slug);

            } catch(Exception e) {
                log.error("Error trying to deserialize post!");
                log.error(e.toString());
                continue;
            }

            log.info("Going to try to send the post to Elasticsearch");

            IndexResponse response = elasticSearch.indexPost(post);

            log.info("Got past indexing post");

            log.info("Created? " + response.isCreated());
            consumerConnector.commitOffsets();
        }
    }
}
