package com.toucan.payments.service;

import com.toucan.payments.dto.TransactionRequest;
import com.toucan.payments.dto.TransactionResponse;
import com.toucan.payments.exception.DuplicateTransactionException;
import com.toucan.payments.exception.InvalidTransactionException;
import com.toucan.payments.exception.TransactionNotFoundException;
import com.toucan.payments.model.Transaction;
import com.toucan.payments.model.TransactionStatus;
import com.toucan.payments.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public TransactionResponse createTransaction(TransactionRequest request) {
        if (repository.existsByTransactionId(request.getTransactionId())) {
            throw new DuplicateTransactionException("Transaction ID already exists: " + request.getTransactionId());
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());

        Transaction saved = repository.save(transaction);
        return mapToResponse(saved);
    }

    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        Transaction transaction = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));
        return mapToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse updateTransactionStatus(String transactionId, TransactionStatus newStatus) {
        Transaction transaction = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionId));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new InvalidTransactionException("Cannot update status from " + transaction.getStatus() + " to " + newStatus + ". Terminal state reached.");
        }

        transaction.setStatus(newStatus);
        Transaction updated = repository.save(transaction);
        return mapToResponse(updated);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionId(),
                transaction.getCustomerId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }
}