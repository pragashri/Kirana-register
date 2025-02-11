package com.example.springboot.feature_transactions.controller;

import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/reports/weekly")
    public Map<String, Double> getWeeklyReport() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        return transactionService.generateReport(oneWeekAgo, now);
    }

    @GetMapping("/reports/monthly")
    public Map<String, Double> getMonthlyReport() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        return transactionService.generateReport(oneMonthAgo, now);
    }

    @GetMapping("/reports/yearly")
    public Map<String, Double> getYearlyReport() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneYearAgo = now.minusYears(1);
        return transactionService.generateReport(oneYearAgo, now);
    }
}
