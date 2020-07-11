package com.example.demo.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CompositeContainerAwareErrorHandler implements ContainerAwareErrorHandler {

    private BinaryExceptionClassifier classifier;

    private final ContainerAwareErrorHandler recoverableErrorHandler;

    private final ContainerAwareErrorHandler unrecoverableErrorHandler;

    public CompositeContainerAwareErrorHandler(ContainerAwareErrorHandler recoverableErrorHandler, ContainerAwareErrorHandler unrecoverableErrorHandler) {
        this.recoverableErrorHandler = recoverableErrorHandler;
        this.unrecoverableErrorHandler = unrecoverableErrorHandler;
        this.classifier = configureDefaultClassifier();
    }

    public CompositeContainerAwareErrorHandler(ContainerAwareErrorHandler recoverableErrorHandler, ContainerAwareErrorHandler unrecoverableErrorHandler, Map<Class<? extends Throwable>, Boolean> classifierTypeMap, boolean classifierDefaultValue) {
        this.recoverableErrorHandler = recoverableErrorHandler;
        this.unrecoverableErrorHandler = unrecoverableErrorHandler;
        BinaryExceptionClassifier binaryExceptionClassifier = new BinaryExceptionClassifier(classifierTypeMap, classifierDefaultValue);
        binaryExceptionClassifier.setTraverseCauses(true);
        this.classifier = binaryExceptionClassifier;
    }

    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
                       MessageListenerContainer container) {
        if (classifier.classify(thrownException)) {
            log.info("Recovering recoverable error {}", thrownException.getMessage());
            recoverableErrorHandler.handle(getRootCause(thrownException), records, consumer, container);
        } else {
            log.info("Recovering unrecoverable error {}", thrownException.getMessage());
            unrecoverableErrorHandler.handle(thrownException, records, consumer, container);
        }
    }

    private Exception getRootCause(Exception thrownException) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(thrownException);
        return rootCause instanceof Exception ? (Exception) rootCause : thrownException;
    }

    private static BinaryExceptionClassifier configureDefaultClassifier() {
        Map<Class<? extends Throwable>, Boolean> classified = new HashMap<>();
        classified.put(ListenerExecutionFailedException.class, true);
        BinaryExceptionClassifier binaryExceptionClassifier = new BinaryExceptionClassifier(classified, false);
        binaryExceptionClassifier.setTraverseCauses(true);
        return binaryExceptionClassifier;
    }

}
