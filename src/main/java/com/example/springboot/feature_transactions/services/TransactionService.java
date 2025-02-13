package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_transactions.entities.Transaction;

import java.time.LocalDateTime;
import java.util.Map;

public interface TransactionService {

    /**
     * interface to find transaction through id
     *
     * @param id
     * @return
     */
    Transaction getTransactionById(String id);

    /**
     * interface to create a transaction
     *
     * @param transaction
     * @return
     */
    Transaction updateTransactionDetails(Transaction transaction);

    /**
     * interface to generate report
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Report generateReport(LocalDateTime startDate, LocalDateTime endDate);
}
