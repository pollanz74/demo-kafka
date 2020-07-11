package com.example.demo.kafka.listener;

import com.example.demo.kafka.domain.Foo;
import com.example.demo.kafka.exception.MyRecoverableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;

@Slf4j
public class MyConsumerRecordRecoverer implements ConsumerRecordRecoverer {

    @Override
    public void accept(ConsumerRecord<?, ?> consumerRecord, Exception e) {
        log.debug("called MyConsumerRecordRecoverer: {}", consumerRecord, e);
        Foo foo = (Foo) consumerRecord.value();
        log.debug("consumerRecord.value(): {}", foo);
        //throw new MyRecoverableException();
    }

}
