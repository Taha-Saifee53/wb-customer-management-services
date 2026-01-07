package com.wb.assignment.controller;

import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
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
                .status(HttpStatus.CREATED)
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

    /* ================= FILTER BY TYPE ================= */

    @Operation(summary = "Get Customer By Type")
    @GetMapping("/type/{type}")
    public ResponseEntity<UnifiedResponse<List<Customer>>> getCustomersByType(
            @PathVariable CustomerType type) {

        var customers = customerService.getCustomersByType(type);
        return ResponseEntity.ok(UnifiedResponse.success(customers, HttpStatus.OK));
    }

    /* ================= FILTER BY STATUS ================= */

    @Operation(summary = "Get Customer by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<UnifiedResponse<List<Customer>>> getCustomersByStatus(
            @PathVariable CustomerStatus status) {

        var customers = customerService.getCustomersByStatus(status);

        return ResponseEntity.ok(UnifiedResponse.success(customers, HttpStatus.OK));
    }

    /* ================= BLOCK CUSTOMER ================= */

    @Operation(summary = "Block Customer by customer Id")
    @PatchMapping("/{customerId}/block")
    public ResponseEntity<UnifiedResponse<Customer>> blockCustomer(
            @PathVariable String customerId) {

        var customer = customerService.blockCustomer(customerId);

        return ResponseEntity.ok(UnifiedResponse.success(customer, HttpStatus.OK));
    }
}

