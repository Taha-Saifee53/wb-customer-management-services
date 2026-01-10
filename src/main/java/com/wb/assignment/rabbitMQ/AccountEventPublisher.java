package com.wb.assignment.rabbitMQ;

import com.wb.assignment.common.Constant;
import com.wb.assignment.request.AccountCreateEvent;
import com.wb.assignment.request.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishAccountCreated(CreateAccountRequest createAccountRequest) {

        var event = AccountCreateEvent.builder()
                .customerId(createAccountRequest.getCustomerId())
                .customerType(createAccountRequest.getCustomerType().name())
                .accountType(createAccountRequest.getAccountType())
                .status(createAccountRequest.getAccountStatus())
                .currency(createAccountRequest.getCurrency())
                .depositAmount(createAccountRequest.getDepositAmount())
                .build();

        rabbitTemplate.convertAndSend(
                Constant.ACCOUNT_EXCHANGE,
                Constant.ACCOUNT_CREATE_ROUTING_KEY,
                event
        );
    }
}


