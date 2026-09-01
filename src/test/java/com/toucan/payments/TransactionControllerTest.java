package com.toucan.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toucan.payments.controller.TransactionController;
import com.toucan.payments.dto.TransactionRequest;
import com.toucan.payments.dto.TransactionResponse;
import com.toucan.payments.model.TransactionStatus;
import com.toucan.payments.model.TransactionType;
import com.toucan.payments.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTransaction_ShouldReturnCreated() throws Exception {
        TransactionRequest request = new TransactionRequest("TX200", "CUST100", new BigDecimal("500.00"), "USD", TransactionType.PAYMENT);
        TransactionResponse response = new TransactionResponse(1L, "TX200", "CUST100", new BigDecimal("500.00"), "USD", TransactionType.PAYMENT, TransactionStatus.PENDING, LocalDateTime.now());

        when(transactionService.createTransaction(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TX200"))
                .andExpect(jsonPath("$.transactionType").value("PAYMENT"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createTransaction_ValidationFailure_ShouldReturnBadRequest() throws Exception {
        TransactionRequest invalidRequest = new TransactionRequest("", "CUST100", new BigDecimal("-50.00"), "", null);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() throws Exception {
        TransactionResponse response = new TransactionResponse(1L, "TX200", "CUST100", new BigDecimal("500.00"), "USD", TransactionType.PAYMENT, TransactionStatus.PENDING, LocalDateTime.now());

        when(transactionService.getTransactionById("TX200")).thenReturn(response);

        mockMvc.perform(get("/api/transactions/TX200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TX200"))
                .andExpect(jsonPath("$.transactionType").value("PAYMENT"));
    }
}