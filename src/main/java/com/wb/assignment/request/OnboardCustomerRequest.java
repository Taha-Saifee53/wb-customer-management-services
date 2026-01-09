package com.wb.assignment.request;

import com.wb.assignment.model.entity.Address;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class OnboardCustomerRequest implements Serializable {

    @NotNull
    @Pattern(regexp = "\\d{7}", message = "Customer ID must be exactly 7 digits")
    private String customerId;
    @NotBlank
    private String name;
    @NotBlank
    private String legalId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    private Address address;
    @Email
    private String email;
    @Pattern(regexp = "\\d{8}", message = "Invalid Phone Number")
    private String phone;
    private double minAmount;

}
