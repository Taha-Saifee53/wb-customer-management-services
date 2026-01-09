package com.wb.assignment.request;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreatedEvent implements Serializable {

    private String customerId;
    private String customerType;
    private String status;
    private double minAmount;
    private LocalDateTime createdAt;
}

