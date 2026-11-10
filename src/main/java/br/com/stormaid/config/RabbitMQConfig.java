package br.com.stormaid.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INCIDENT_CREATED_QUEUE = "incident.created.queue";

    @Bean
    public Queue incidentCreatedQueue() {
        return new Queue(INCIDENT_CREATED_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}