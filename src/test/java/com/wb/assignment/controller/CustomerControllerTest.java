package com.wb.assignment.controller;

import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import com.wb.assignment.request.OnboardCustomerRequest;
import com.wb.assignment.response.UnifiedResponse;
import com.wb.assignment.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* ================= ONBOARD CUSTOMER ================= */

    @Test
    void createCustomer_shouldReturnSavedCustomer() {
        var request = OnboardCustomerRequest.builder()
                .name("Taha Saifee")
                .legalId("322060301853")
                .customerType(CustomerType.CORPORATE)
                .status(CustomerStatus.ACTIVE)
                .build();

        var savedCustomer = Customer.builder()
                .customerId("322060301853")
                .name("Taha Saifee")
                .customerType(CustomerType.CORPORATE)
                .status(CustomerStatus.ACTIVE)
                .build();
        when(customerService.createCustomer(request)).thenReturn(savedCustomer);
        ResponseEntity<UnifiedResponse<Customer>> response = customerController.createCustomer(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getResponse()).isEqualTo(savedCustomer);
        verify(customerService, times(1)).createCustomer(request);
    }

    /* ================= GET CUSTOMER BY ID ================= */

    @Test
    void getCustomerById_shouldReturnCustomer() {
        var customerId = "1234567";
        var customer = Customer.builder()
                .customerId(customerId)
                .name("Taha Saifee")
                .customerType(CustomerType.CORPORATE)
                .status(CustomerStatus.ACTIVE)
                .build();

        when(customerService.getCustomerById(customerId)).thenReturn(customer);
        ResponseEntity<UnifiedResponse<Customer>> response = customerController.getCustomerById(customerId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getResponse()).isEqualTo(customer);
        verify(customerService, times(1)).getCustomerById(customerId);
    }

    /* ================= GET ALL CUSTOMERS ================= */

    @Test
    void getAllCustomers_shouldReturnList() {
        List<Customer> customers = List.of(
                Customer.builder().customerId("1234567").name("Taha").build(),
                Customer.builder().customerId("2345678").name("Qusai").build()
        );
        when(customerService.getAllCustomers()).thenReturn(customers);
        ResponseEntity<UnifiedResponse<List<Customer>>> response = customerController.getAllCustomers();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getResponse()).isEqualTo(customers);
        verify(customerService, times(1)).getAllCustomers();
    }
}
