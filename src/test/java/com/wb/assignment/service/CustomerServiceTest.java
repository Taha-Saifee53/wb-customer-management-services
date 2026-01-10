package com.wb.assignment.service;

import com.wb.assignment.exception.BusinessException;
import com.wb.assignment.model.entity.Address;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import com.wb.assignment.rabbitMQ.AccountEventPublisher;
import com.wb.assignment.repository.CustomerRepository;
import com.wb.assignment.request.OnboardCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountEventPublisher eventPublisher;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* ================= TEST ONBOARD CUSTOMER ================= */

    @Test
    void createCustomer_shouldSaveAndPublishEvent() {
        var address = Address.builder().id(1L).build();
        var request = OnboardCustomerRequest.builder()
                .customerId("1234567")
                .name("Taha Saifee")
                .customerType(CustomerType.RETAIL)
                .status(CustomerStatus.ACTIVE)
                .legalId("322060301853")
                .address(address)
                .build();
        var customerToSave = Customer.builder()
                .customerId("1234567")
                .name("Taha Saifee")
                .customerType(CustomerType.RETAIL)
                .status(CustomerStatus.ACTIVE)
                .legalId("322060301853")
                .address(address)
                .createdAt(LocalDateTime.now())
                .build();
        when(customerRepository.existsByCustomerId(request.getCustomerId())).thenReturn(false);
        when(customerRepository.existsByLegalId(request.getLegalId())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customerToSave);
        var saved = customerService.createCustomer(request);
        assertThat(saved).isEqualTo(customerToSave);
        // Verify repository save and event published
        verify(customerRepository, times(1)).save(any(Customer.class));
    }


    @Test
    void createCustomer_shouldNotSaveAndPublishEvent() {
        var address = Address.builder().id(1L).build();
        var request = OnboardCustomerRequest.builder()
                .customerId("1234567")
                .name("Taha Saifee")
                .customerType(CustomerType.RETAIL)
                .status(CustomerStatus.ACTIVE)
                .legalId("322060301853")
                .address(address)
                .build();
        var customerToSave = Customer.builder()
                .customerId(null)
                .name("Taha Saifee")
                .customerType(CustomerType.RETAIL)
                .status(CustomerStatus.ACTIVE)
                .legalId("322060301853")
                .address(address)
                .createdAt(LocalDateTime.now())
                .build();
        when(customerRepository.existsByCustomerId(request.getCustomerId())).thenReturn(false);
        when(customerRepository.existsByLegalId(request.getLegalId())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customerToSave);
        var ex = assertThrows(BusinessException.class,
                () -> customerService.createCustomer(request));
        assertThat(ex.getErrorCode()).isEqualTo("APPLICATION_ERROR");
    }
    @Test
    void createCustomer_shouldThrowBusinessException_ifDuplicateCustomerId() {
        var request = OnboardCustomerRequest.builder()
                .customerId("1234567")
                .legalId("322060301853")
                .build();
        when(customerRepository.existsByCustomerId("1234567")).thenReturn(true);

        var ex = assertThrows(BusinessException.class,
                () -> customerService.createCustomer(request));
        assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE_CUSTOMER_ID");
    }

    @Test
    void createCustomer_shouldThrowBusinessException_ifDuplicateLegalId() {
        var request = OnboardCustomerRequest.builder()
                .customerId("1234567")
                .legalId("322060301853")
                .build();

        when(customerRepository.existsByCustomerId("1234567")).thenReturn(false);
        when(customerRepository.existsByLegalId("322060301853")).thenReturn(true);
        var ex = assertThrows(BusinessException.class,
                () -> customerService.createCustomer(request));
        assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE_LEGAL_ID");
    }

    /* ================= TEST GET CUSTOMER BY ID ================= */

    @Test
    void getCustomerById_shouldReturnCustomer() {
        var customer = Customer.builder().customerId("1234567").build();
        when(customerRepository.findById("1234567")).thenReturn(Optional.of(customer));
        var result = customerService.getCustomerById("1234567");
        assertThat(result).isEqualTo(customer);
    }

    @Test
    void getCustomerById_shouldThrowEntityNotFoundException_ifNotFound() {
        when(customerRepository.findById("1234567")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> customerService.getCustomerById("1234567"));
    }

    /* ================= TEST GET ALL CUSTOMERS ================= */

    @Test
    void getAllCustomers_shouldReturnList() {
        List<Customer> customers = List.of(
                Customer.builder().customerId("1234567").build(),
                Customer.builder().customerId("2345678").build()
        );
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> result = customerService.getAllCustomers();
        assertThat(result).isEqualTo(customers);
    }
}
