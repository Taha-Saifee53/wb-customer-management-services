package com.wb.assignment.repository;

import com.wb.assignment.model.entity.Customer;
import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /* ---------- FINDERS ---------- */

    Optional<Customer> findByCustomerId(String customerId);

    Optional<Customer> findByLegalId(String legalId);

    List<Customer> findByCustomerType(CustomerType customerType);

    List<Customer> findByStatus(CustomerStatus status);

    /* ---------- EXISTENCE CHECKS (BUSINESS RULES) ---------- */

    boolean existsByCustomerId(String customerId);

    boolean existsByLegalId(String legalId);

    List<Customer> findByCustomerTypeAndStatus(
            CustomerType customerType,
            CustomerStatus status
    );
}

