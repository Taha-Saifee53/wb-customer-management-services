package com.wb.assignment.request;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateEvent implements Serializable {

    private String customerId;
    private String customerType;
    private AccountType accountType;
    private AccountStatus status;
    private String currency;
    private double depositAmount;
}

