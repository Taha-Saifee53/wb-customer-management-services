package com.wb.assignment.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.wb.assignment.common.Constant.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(ACCOUNT_EXCHANGE);
    }

    @Bean
    public Queue customerCreatedQueue() {
        return new Queue(ACCOUNT_CREATE_QUEUE, true);
    }

    @Bean
    public Binding customerCreatedBinding() {
        return BindingBuilder
                .bind(customerCreatedQueue())
                .to(customerExchange())
                .with(ACCOUNT_CREATE_ROUTING_KEY);
    }
}

