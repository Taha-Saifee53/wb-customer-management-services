package com.wb.assignment.service;

import com.wb.assignment.exception.BusinessException;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {

        log.info("Create Customer");
        if (customerRepository.existsByCustomerId(customer.getCustomerId())) {
            throw new BusinessException(
                    "DUPLICATE_CUSTOMER_ID",
                    "Customer ID already exists"
            );
        }

        if (customerRepository.existsByLegalId(customer.getLegalId())) {
            throw new BusinessException(
                    "DUPLICATE_LEGAL_ID",
                    "Legal ID already exists"
            );
        }

        return customerRepository.save(customer);
    }

    public Customer getCustomerById(String customerId) {
        log.info("Fetch Customer Details by Id");
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + customerId));
    }

    public List<Customer> getAllCustomers() {
        log.info("Fetch all customer details");
        return customerRepository.findAll();
    }

}

