package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_transactions.entities.Transaction;

import java.time.LocalDateTime;
import java.util.Map;

public interface TransactionService {
    Transaction getTransactionById(String id);
    Transaction createTransaction(Transaction transaction);
    Map<String, Double> generateReport(LocalDateTime startDate, LocalDateTime endDate);
}
