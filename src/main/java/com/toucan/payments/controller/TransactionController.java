package com.toucan.payments.controller;

import com.toucan.payments.dto.TransactionRequest;
import com.toucan.payments.dto.TransactionResponse;
import com.toucan.payments.model.TransactionStatus;
import com.toucan.payments.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable String transactionId) {
        TransactionResponse response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> responses = transactionService.getAllTransactions();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCustomerId(@PathVariable String customerId) {
        List<TransactionResponse> responses = transactionService.getTransactionsByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(
            @PathVariable String transactionId,
            @RequestParam TransactionStatus status) {
        TransactionResponse response = transactionService.updateTransactionStatus(transactionId, status);
        return ResponseEntity.ok(response);
    }
}