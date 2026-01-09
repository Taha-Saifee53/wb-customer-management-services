package com.wb.assignment.service;

import com.wb.assignment.exception.BusinessException;
import com.wb.assignment.rabbitMQ.CustomerEventPublisher;
import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.repository.CustomerRepository;
import com.wb.assignment.request.OnboardCustomerRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventPublisher eventPublisher;

    @Transactional
    public Customer createCustomer(OnboardCustomerRequest onboardCustomerRequest) {

        log.info("Onboard New Customer");
        var customer = mapOnboardCustomer(onboardCustomerRequest);
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

        var savedCustomerDetails =  customerRepository.save(customer);
        if (!Objects.isNull(savedCustomerDetails.getCustomerId())) {
            log.info("Publish Customer Details");
            eventPublisher.publishCustomerCreated(savedCustomerDetails, onboardCustomerRequest.getMinAmount());
        } else {
            log.info("Customer Details Not saved");
            throw new BusinessException("APPLICATION_ERROR","Customer Details Not Saved");
        }

        return savedCustomerDetails;
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

    private Customer mapOnboardCustomer(OnboardCustomerRequest onboardCustomerRequest) {
        return Customer.builder()
                .customerId(onboardCustomerRequest.getCustomerId())
                .name(onboardCustomerRequest.getName())
                .customerType(onboardCustomerRequest.getCustomerType())
                .status(onboardCustomerRequest.getStatus())
                .phone(onboardCustomerRequest.getPhone())
                .email(onboardCustomerRequest.getEmail())
                .legalId(onboardCustomerRequest.getLegalId())
                .address(onboardCustomerRequest.getAddress())
                .build();
    }

}

