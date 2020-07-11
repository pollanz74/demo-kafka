package com.example.demo.kafka.listener;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.kafka.listener.ContainerAwareBatchErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CompositeContainerAwareBatchErrorHandler implements ContainerAwareBatchErrorHandler {

    private BinaryExceptionClassifier classifier;

    private final ContainerAwareBatchErrorHandler recoverableErrorHandler;

    private final ContainerAwareBatchErrorHandler unrecoverableErrorHandler;

    public CompositeContainerAwareBatchErrorHandler(@NonNull final ContainerAwareBatchErrorHandler recoverableErrorHandler, @NonNull final ContainerAwareBatchErrorHandler unrecoverableErrorHandler) {
        this(configureDefaultClassifier(), recoverableErrorHandler, unrecoverableErrorHandler);
    }

    public CompositeContainerAwareBatchErrorHandler(@NonNull final BinaryExceptionClassifier classifier, @NonNull final ContainerAwareBatchErrorHandler recoverableErrorHandler, @NonNull final ContainerAwareBatchErrorHandler unrecoverableErrorHandler) {
        this.classifier = classifier;
        this.recoverableErrorHandler = recoverableErrorHandler;
        this.unrecoverableErrorHandler = unrecoverableErrorHandler;
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data, Consumer<?, ?> consumer, MessageListenerContainer container) {
        if (Boolean.TRUE.equals(classifier.classify(getMostSpecificCause(thrownException)))) {
            log.info("Handling unrecoverable error {}", thrownException.getMessage());
            unrecoverableErrorHandler.handle(thrownException, data, consumer, container);
        } else {
            log.info("Handling recoverable error {}", thrownException.getMessage());
            recoverableErrorHandler.handle(thrownException, data, consumer, container);
        }
    }

    private Exception getMostSpecificCause(Exception thrownException) {
        Throwable rootCause = NestedExceptionUtils.getMostSpecificCause(thrownException);
        return rootCause instanceof Exception ? (Exception) rootCause : thrownException;
    }

    private static BinaryExceptionClassifier configureDefaultClassifier() {
        final Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap<>();
        exceptions.put(org.apache.kafka.common.KafkaException.class, true);
        exceptions.put(org.springframework.kafka.KafkaException.class, true);
        exceptions.put(ListenerExecutionFailedException.class, false);

        final BinaryExceptionClassifier classifier = new BinaryExceptionClassifier(false);
        classifier.setTypeMap(exceptions);
        classifier.setTraverseCauses(true);
        return classifier;
    }

}
