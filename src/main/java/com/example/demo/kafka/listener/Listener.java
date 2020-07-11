package com.example.demo.kafka.listener;

import com.example.demo.kafka.domain.Foo;
import com.example.demo.kafka.exception.MyRecoverableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

@Slf4j
public class Listener {

    @KafkaListener(topics = "${demo.foo.command.topic.name}", containerFactory = "batchFactory")
    public void listen(final List<Foo> foos) {
        log.debug("foos: {}", foos);
        foos.forEach(foo -> {
                    log.info("Received: " + foo);
                    if (foo.getFoo().startsWith("unrecover")) {
                        throw new KafkaException("fatal error");
                    }

                    if (foo.getFoo().startsWith("recover")) {
                        throw new MyRecoverableException();
                    }

                }
        );

    }

}
