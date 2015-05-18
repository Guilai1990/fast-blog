/* Originally written by Confluent: https://github.com/confluentinc/examples/blob/master/consumer/src/main/java/io/confluent/examples/consumer/ConsumerGroupExample.java
Original license follows
 */
/**
 * Copyright 2015 Confluent Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wkh.fastblog.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.javaapi.consumer.ConsumerConnector;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wkh.fastblog.domain.PostRecord;
import org.wkh.fastblog.pg.PostDAO;
import org.wkh.fastblog.serialization.SerializationService;

@Component
public class PGPostConsumerThread implements Runnable  {
    private Logger log = LoggerFactory.getLogger(PGPostConsumerThread.class);
    private KafkaStream stream;
    private ConsumerConnector consumerConnector;
    private PostDAO dao;

    public PGPostConsumerThread() {

    }

    private SerializationService serializationService;

    public PGPostConsumerThread(KafkaStream stream,
                                SerializationService serializationService,
                                ConsumerConnector consumerConnector,
                                PostDAO dao) {
        this.stream = stream;
        this.serializationService = serializationService;
        this.consumerConnector = consumerConnector;
        this.dao = dao;
    }

    public void run() {
        log.info("Consumer thread started");

        ConsumerIterator<byte[], byte[]> it = stream.iterator();

        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> record = it.next();
            byte[] message = record.message();

            if (serializationService == null) {
                log.error("Serialization service is null!");
                continue;
            }

            log.info("Going to deserialize post");

            PostRecord postRecord;

            try {
                postRecord = serializationService.deserializePost(message);
            } catch(Exception e) {
                log.error("Error trying to deserialize post!");
                log.error(e.toString());
                continue;
            }

            log.info("Going to try to send the post to Postgresql");

            dao.insertRecord(postRecord);

            log.info("We got to after the insert call");

            consumerConnector.commitOffsets();
        }

        log.info("Shutting down thread");
    }
}