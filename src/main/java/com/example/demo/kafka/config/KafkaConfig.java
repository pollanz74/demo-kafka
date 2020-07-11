package com.example.demo.kafka.config;

import com.example.demo.kafka.exception.MyRecoverableException;
import com.example.demo.kafka.listener.CompositeContainerAwareErrorHandler;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.ContainerStoppingErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class KafkaConfig {

//    @Value("${demo.foo.command.topic.name.dlt}")
//    private String dltTopicName;
//
//    @Bean
//    ContainerAwareErrorHandler configureErrorHandler(KafkaOperations<Object, Object> template) {
//        final SeekToCurrentErrorHandler recoverableErrorHandler = errorHandler(template);
//        final ContainerAwareErrorHandler unrecoverableErrorHandler = new ContainerStoppingErrorHandler();
//
//        return new CompositeContainerAwareErrorHandler(recoverableErrorHandler, unrecoverableErrorHandler);
//    }
//
//    private DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaOperations<Object, Object> template) {
//        return new DeadLetterPublishingRecoverer(template, (cr, e) -> new TopicPartition(dltTopicName, cr.partition()));
//    }
//
//    private SeekToCurrentErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
//        SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(
//                deadLetterPublishingRecoverer(template),
//                new FixedBackOff(10000L, 2)
//        );
//        errorHandler.setClassifications(recoverableErrorHandlerClassifications(), false);
//        return errorHandler;
//    }
//
//    private Map<Class<? extends Throwable>, Boolean> recoverableErrorHandlerClassifications() {
//        final Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap<>();
//        exceptions.put(MyRecoverableException.class, true);
//        return exceptions;
//    }

}
