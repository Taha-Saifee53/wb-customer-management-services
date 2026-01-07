package com.wb.assignment.controller;

import com.wb.assignment.model.entity.Customer;
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

    /* ================= CREATE CUSTOMER ================= */

    @Operation(summary = "Create a new customer")
    @PostMapping
    public ResponseEntity<UnifiedResponse<Customer>> createCustomer(
            @Valid @RequestBody Customer customer) {

        var savedCustomer = customerService.createCustomer(customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UnifiedResponse.success(savedCustomer, HttpStatus.OK));
    }

    /* ================= GET CUSTOMER BY ID ================= */

    @Operation(summary = "Get customer By Id")
    @GetMapping("/{customerId}")
    public ResponseEntity<UnifiedResponse<Customer>> getCustomerById(
            @PathVariable String customerId) {

        var customer = customerService.getCustomerById(customerId);

        return ResponseEntity.ok(UnifiedResponse.success(customer, HttpStatus.OK));
    }

    /* ================= GET ALL CUSTOMERS ================= */

    @Operation(summary = "Get All Customers")
    @GetMapping
    public ResponseEntity<UnifiedResponse<List<Customer>>> getAllCustomers() {

        var customers = customerService.getAllCustomers();

        return ResponseEntity.ok(UnifiedResponse.success(customers, HttpStatus.OK));
    }
}

