package com.wb.assignment.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.amqp.core.*;

import static org.assertj.core.api.Assertions.assertThat;
import static com.wb.assignment.common.Constant.*;

@SpringBootTest(classes = RabbitMQConfig.class)
class RabbitMQConfigTest {

    @Autowired
    private TopicExchange customerExchange;

    @Autowired
    private Queue customerCreatedQueue;

    @Autowired
    private Binding customerCreatedBinding;

    @Test
    void shouldCreateCustomerExchange() {
        assertThat(customerExchange).isNotNull();
        assertThat(customerExchange.getName()).isEqualTo(CUSTOMER_EXCHANGE);
        assertThat(customerExchange.getType()).isEqualTo(ExchangeTypes.TOPIC);
    }

    @Test
    void shouldCreateCustomerCreatedQueue() {
        assertThat(customerCreatedQueue).isNotNull();
        assertThat(customerCreatedQueue.getName()).isEqualTo(CUSTOMER_CREATED_QUEUE);
        assertThat(customerCreatedQueue.isDurable()).isTrue();
    }

    @Test
    void shouldBindQueueToExchangeWithRoutingKey() {
        assertThat(customerCreatedBinding).isNotNull();
        assertThat(customerCreatedBinding.getExchange()).isEqualTo(CUSTOMER_EXCHANGE);
        assertThat(customerCreatedBinding.getDestination()).isEqualTo(CUSTOMER_CREATED_QUEUE);
        assertThat(customerCreatedBinding.getRoutingKey()).isEqualTo(CUSTOMER_CREATED_ROUTING_KEY);
        assertThat(customerCreatedBinding.getDestinationType())
                .isEqualTo(Binding.DestinationType.QUEUE);
    }
}

