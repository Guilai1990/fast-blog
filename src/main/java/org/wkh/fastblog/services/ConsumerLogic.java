package org.wkh.fastblog.services;

import org.apache.avro.generic.GenericRecord;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerLogic implements Runnable {
    private Logger log = LoggerFactory.getLogger(ConsumerLogic.class);
    private KafkaStream stream;
    private int threadNumber;

    public ConsumerLogic(KafkaStream stream, int threadNumber) {
        this.threadNumber = threadNumber;
        this.stream = stream;
    }

    public void run() {
        for (MessageAndMetadata<Object, Object> record : (Iterable<MessageAndMetadata<Object, Object>>) stream) {
            String topic = record.topic();
            int partition = record.partition();
            long offset = record.offset();
            Object key = record.key();
            GenericRecord message = (GenericRecord) record.message();

            log.info("Thread " + threadNumber +
                    " received: " + "Topic " + topic +
                    " Partition " + partition +
                    " Offset " + offset +
                    " Key " + key +
                    " Message " + message.toString());
        }
        
        log.info("Shutting down Thread: " + threadNumber);
    }
}
