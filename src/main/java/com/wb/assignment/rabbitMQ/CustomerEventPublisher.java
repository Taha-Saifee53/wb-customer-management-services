package com.wb.assignment.rabbitMQ;

import com.wb.assignment.common.Constant;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.request.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCustomerCreated(Customer customer, double minAmount) {

        var event = CustomerCreatedEvent.builder()
                .customerId(customer.getCustomerId())
                .customerType(customer.getCustomerType().name())
                .status(customer.getStatus().name())
                .createdAt(customer.getCreatedAt())
                .minAmount(minAmount)
                .build();

        rabbitTemplate.convertAndSend(
                Constant.CUSTOMER_EXCHANGE,
                Constant.CUSTOMER_CREATED_ROUTING_KEY,
                event
        );
    }
}


