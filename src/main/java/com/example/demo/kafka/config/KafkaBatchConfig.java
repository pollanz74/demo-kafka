package com.example.demo.kafka.config;

import com.example.demo.kafka.listener.CompositeContainerAwareBatchErrorHandler;
import com.example.demo.kafka.listener.MyConsumerRecordRecoverer;
import com.example.demo.kafka.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.listener.adapter.DefaultBatchToRecordAdapter;

@Slf4j
@Configuration
public class KafkaBatchConfig {

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> batchFactory(
            final ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            final ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        log.debug("consumerFactory.getConfigurationProperties(): {}", consumerFactory.getConfigurationProperties());
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setBatchToRecordAdapter(new DefaultBatchToRecordAdapter<>(consumerRecordRecoverer()));
        factory.getContainerProperties().setDeliveryAttemptHeader(true);
        configurer.configure(factory, consumerFactory);
        return factory;
    }

    @Bean
    ConsumerRecordRecoverer consumerRecordRecoverer() {
        return new MyConsumerRecordRecoverer();
    }

    //@Bean
    ContainerAwareBatchErrorHandler batchErrorHandler() {
        final SeekToCurrentBatchErrorHandler recoverableErrorHandler = new SeekToCurrentBatchErrorHandler();
        final ContainerAwareBatchErrorHandler unrecoverableErrorHandler = new ContainerStoppingBatchErrorHandler();

        return new CompositeContainerAwareBatchErrorHandler(recoverableErrorHandler, unrecoverableErrorHandler);
    }

    @Bean
    Listener listener() {
        return new Listener();
    }

}
