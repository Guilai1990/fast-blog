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

package org.wkh.fastblog.services;

import kafka.consumer.ConsumerIterator;
import kafka.serializer.Decoder;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.avro.io.DatumReader;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wkh.fastblog.domain.Post;

public class PostConsumerThread implements Runnable {
    private Logger log = LoggerFactory.getLogger(PostConsumerThread.class);
    private KafkaStream stream;

    @Autowired
    private RedisService redisService;

    public PostConsumerThread(KafkaStream stream) {
        this.stream = stream;
    }

    public void run() {
        log.info("Consumer thread started");
        ConsumerIterator<Object, Object> it = stream.iterator();

        while (it.hasNext()) {
            MessageAndMetadata<Object, Object> record = it.next();
            GenericRecord message = (GenericRecord) record.message();

            log.info("Going to try to reconstruct...");

            Post post = Post.fromRecord(message);

            log.info("Reconstruction worked! ID is: " + post.getId());

            redisService.addPostToList(post);
        }

        log.info("Shutting down thread");
    }
}
