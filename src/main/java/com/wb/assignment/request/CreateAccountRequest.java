package com.wb.assignment.request;

import com.wb.assignment.model.enums.CustomerType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest implements Serializable {

    @NotNull
    @Pattern(regexp = "\\d{7}", message = "Customer ID must be exactly 7 digits")
    private String customerId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @Min(1)
    private double depositAmount;

}
