package com.toucan.payments;

import com.toucan.payments.dto.TransactionRequest;
import com.toucan.payments.dto.TransactionResponse;
import com.toucan.payments.exception.DuplicateTransactionException;
import com.toucan.payments.exception.InvalidTransactionException;
import com.toucan.payments.exception.TransactionNotFoundException;
import com.toucan.payments.model.Transaction;
import com.toucan.payments.model.TransactionStatus;
import com.toucan.payments.model.TransactionType;
import com.toucan.payments.repository.TransactionRepository;
import com.toucan.payments.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionServiceImpl service;

    @Test
    void createTransaction_ShouldReturnResponse() {
        TransactionRequest request = new TransactionRequest("TX100", "CUST100", new BigDecimal("100.00"), "USD", TransactionType.PAYMENT);
        Transaction saved = new Transaction(1L, "TX100", "CUST100", new BigDecimal("100.00"), "USD", TransactionType.PAYMENT, TransactionStatus.PENDING, LocalDateTime.now());

        when(repository.existsByTransactionId("TX100")).thenReturn(false);
        when(repository.save(any(Transaction.class))).thenReturn(saved);

        TransactionResponse response = service.createTransaction(request);

        assertNotNull(response);
        assertEquals("TX100", response.getTransactionId());
        assertEquals(TransactionType.PAYMENT, response.getTransactionType());
        assertEquals(TransactionStatus.PENDING, response.getStatus());
    }

    @Test
    void createTransaction_DuplicateId_ShouldThrowException() {
        TransactionRequest request = new TransactionRequest("TX100", "CUST100", new BigDecimal("100.00"), "USD", TransactionType.PAYMENT);
        when(repository.existsByTransactionId("TX100")).thenReturn(true);

        assertThrows(DuplicateTransactionException.class, () -> service.createTransaction(request));
    }

    @Test
    void getTransactionById_NotFound_ShouldThrowException() {
        when(repository.findByTransactionId("TX999")).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> service.getTransactionById("TX999"));
    }

    @Test
    void updateStatus_WhenTerminalState_ShouldThrowException() {
        Transaction existing = new Transaction(1L, "TX100", "CUST100", new BigDecimal("100.00"), "USD", TransactionType.PAYMENT, TransactionStatus.SUCCESS, LocalDateTime.now());

        when(repository.findByTransactionId("TX100")).thenReturn(Optional.of(existing));

        assertThrows(InvalidTransactionException.class, () -> 
            service.updateTransactionStatus("TX100", TransactionStatus.FAILED)
        );
    }
}