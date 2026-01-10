package com.wb.assignment.repository;

import com.wb.assignment.model.entity.Account;
import com.wb.assignment.request.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    long countByCustomer_CustomerId(String customerId);
    boolean existsByCustomer_CustomerIdAndAccountType(String customerId, AccountType accountType);
    List<Account> findByCustomer_CustomerId(String customerId);
}

