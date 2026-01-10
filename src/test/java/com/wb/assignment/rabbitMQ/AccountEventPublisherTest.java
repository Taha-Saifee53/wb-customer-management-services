package com.wb.assignment.rabbitMQ;

import com.wb.assignment.common.Constant;
import com.wb.assignment.model.enums.CustomerType;
import com.wb.assignment.request.AccountCreateEvent;
import com.wb.assignment.request.AccountStatus;
import com.wb.assignment.request.AccountType;
import com.wb.assignment.request.CreateAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

class AccountEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private AccountEventPublisher publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new AccountEventPublisher(rabbitTemplate);
    }

    @Test
    void publishCustomerCreated_shouldCallRabbitTemplate() {
        var createAccount = CreateAccountRequest.builder()
                .customerId("1234567")
                .customerType(CustomerType.CORPORATE)
                .accountType(AccountType.CURRENT)
                .accountStatus(AccountStatus.ACTIVE)
                .depositAmount(1000.0)
                .build();

        publisher.publishAccountCreated(createAccount);
        // Capture argument sent to RabbitTemplate
        ArgumentCaptor<AccountCreateEvent> captor = ArgumentCaptor.forClass(AccountCreateEvent.class);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(Constant.ACCOUNT_EXCHANGE),
                eq(Constant.ACCOUNT_CREATE_ROUTING_KEY),
                captor.capture()
        );
        var eventSent = captor.getValue();

        // Verify that fields are set correctly
        assert eventSent.getCustomerId().equals(createAccount.getCustomerId());
        assert eventSent.getCustomerType().equals(createAccount.getCustomerType().name());
        assert eventSent.getStatus().equals(createAccount.getAccountStatus());
        assert eventSent.getDepositAmount() == createAccount.getDepositAmount();
    }
}
