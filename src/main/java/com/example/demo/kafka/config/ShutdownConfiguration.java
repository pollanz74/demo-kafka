package com.example.demo.kafka.config;

import com.example.demo.kafka.shutdown.ApplicationShutdown;
import com.example.demo.kafka.shutdown.ContainerListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownConfiguration {

    @Bean
    public ApplicationShutdown applicationShutdown(final ConfigurableApplicationContext applicationContext) {
        return new ApplicationShutdown(applicationContext);
    }

    @Bean
    public ContainerListener containerListener(final ApplicationShutdown applicationShutdown) {
        return new ContainerListener(applicationShutdown);
    }

}
