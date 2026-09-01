package com.toucan.payments.service;

import com.toucan.payments.dto.TransactionRequest;
import com.toucan.payments.dto.TransactionResponse;
import com.toucan.payments.model.TransactionStatus;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);
    TransactionResponse getTransactionById(String transactionId);
    List<TransactionResponse> getAllTransactions();
    List<TransactionResponse> getTransactionsByCustomerId(String customerId);
    TransactionResponse updateTransactionStatus(String transactionId, TransactionStatus newStatus);
}