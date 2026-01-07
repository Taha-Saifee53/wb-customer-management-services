package com.wb.assignment.service;

import com.wb.assignment.exception.BusinessException;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import com.wb.assignment.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {

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
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID: " + customerId));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getCustomersByType(CustomerType type) {
        return customerRepository.findByCustomerType(type);
    }

    public List<Customer> getCustomersByStatus(CustomerStatus status) {
        return customerRepository.findByStatus(status);
    }

    @Transactional
    public Customer blockCustomer(String customerId) {
        Customer customer = getCustomerById(customerId);
        customer.setStatus(CustomerStatus.BLOCKED);
        return customerRepository.save(customer);
    }

}

