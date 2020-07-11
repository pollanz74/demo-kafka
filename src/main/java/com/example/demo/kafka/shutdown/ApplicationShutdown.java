package com.example.demo.kafka.shutdown;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@RequiredArgsConstructor
public class ApplicationShutdown {

    private final ConfigurableApplicationContext applicationContext;

    public void shutdownApplication() {
        if (applicationContext.isActive() && applicationContext.isRunning()) {
            log.info("Shutting down application");
            System.exit(SpringApplication.exit(applicationContext, () -> 0));
        } else {
            log.info("Application is already shutting down");
        }
    }

}
