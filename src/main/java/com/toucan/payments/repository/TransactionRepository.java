package com.toucan.payments.repository;

import com.toucan.payments.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByTransactionId(String transactionId);
    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByCustomerId(String customerId);
}