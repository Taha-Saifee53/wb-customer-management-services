package com.wb.assignment.controller;

import com.wb.assignment.model.entity.Account;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.repository.AccountRepository;
import com.wb.assignment.request.CreateAccountRequest;
import com.wb.assignment.request.OnboardCustomerRequest;
import com.wb.assignment.response.UnifiedResponse;
import com.wb.assignment.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AccountRepository accountRepository;

    /* ================= ONBOARD NEW CUSTOMER ================= */

    @Operation(summary = "Onboard a new customer")
    @PostMapping("onboard-customer")
    public ResponseEntity<UnifiedResponse<Customer>> createCustomer(
            @Valid @RequestBody OnboardCustomerRequest onboardCustomerRequest) {

        var savedCustomer = customerService.createCustomer(onboardCustomerRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UnifiedResponse.success(savedCustomer, HttpStatus.OK));
    }

    /* ================= GET ONBOARDED CUSTOMER BY ID ================= */

    @Operation(summary = "Get Onboarded customer By Id")
    @GetMapping("/{customerId}")
    public ResponseEntity<UnifiedResponse<Customer>> getCustomerById(
            @PathVariable String customerId) {
        var customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(UnifiedResponse.success(customer, HttpStatus.OK));
    }

    /* ================= GET ALL ONBOARDED CUSTOMERS ================= */

    @Operation(summary = "Get All Onboarded Customers")
    @GetMapping("list-onboard-customers")
    public ResponseEntity<UnifiedResponse<List<Customer>>> getAllCustomers() {
        var customers = customerService.getAllCustomers();
        return ResponseEntity.ok(UnifiedResponse.success(customers, HttpStatus.OK));
    }

    /* ================= CREATE NEW ACCOUNT ================= */

    @Operation(summary = "Create a new account")
    @PostMapping("create-account")
    public ResponseEntity<UnifiedResponse<String>> createAccount(
            @Valid @RequestBody CreateAccountRequest createAccountRequest) {

        var savedCustomer = customerService.createAccount(createAccountRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UnifiedResponse.success(savedCustomer, HttpStatus.OK));
    }

    /* ================= GET ACCOUNTS CUSTOMER BY ID ================= */

    @Operation(summary = "Get All Accounts By Customer Id")
    @GetMapping("/fecth-accounts/{customerId}")
    public ResponseEntity<UnifiedResponse<List<Account>>> getAllAccounts(
            @PathVariable String customerId) {
        var accounts = accountRepository.findByCustomer_CustomerId(customerId);
        return ResponseEntity.ok(UnifiedResponse.success(accounts, HttpStatus.OK));
    }


}

