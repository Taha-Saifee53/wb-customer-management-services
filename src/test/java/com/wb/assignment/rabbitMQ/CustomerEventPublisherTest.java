package com.wb.assignment.rabbitMQ;

import com.wb.assignment.common.Constant;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import com.wb.assignment.request.CustomerCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class CustomerEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private CustomerEventPublisher publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new CustomerEventPublisher(rabbitTemplate);
    }

    @Test
    void publishCustomerCreated_shouldCallRabbitTemplate() {
        Customer customer = Customer.builder()
                .customerId("1234567")
                .customerType(CustomerType.RETAIL)
                .status(CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        double minAmount = 1000.0;
        publisher.publishCustomerCreated(customer, minAmount);
        // Capture argument sent to RabbitTemplate
        ArgumentCaptor<CustomerCreatedEvent> captor = ArgumentCaptor.forClass(CustomerCreatedEvent.class);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(Constant.CUSTOMER_EXCHANGE),
                eq(Constant.CUSTOMER_CREATED_ROUTING_KEY),
                captor.capture()
        );
        var eventSent = captor.getValue();

        // Verify that fields are set correctly
        assert eventSent.getCustomerId().equals(customer.getCustomerId());
        assert eventSent.getCustomerType().equals(customer.getCustomerType().name());
        assert eventSent.getStatus().equals(customer.getStatus().name());
        assert eventSent.getMinAmount() == minAmount;
        assert eventSent.getCreatedAt().equals(customer.getCreatedAt());
    }
}
