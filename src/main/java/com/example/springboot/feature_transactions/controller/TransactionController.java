package com.example.springboot.feature_transactions.controller;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
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


    /**
     * API call to create a transaction
     *
     * @param transaction
     * @return
     */
    @PostMapping("/update")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.updateTransactionDetails(transaction);
    }


    /**
     *
     * API call to fetch transaction through transaction id
     * @param id
     * @return
     */
    @GetMapping("/fetch")
    public Transaction getTransactionById(@RequestParam String id) {
        return transactionService.getTransactionById(id);
    }


    /**
     * API call to get report FOR WEEKLY, MONTHLY or YEARLY
     *
     * @return
     */
    @GetMapping("/reports")
    public Report getWeeklyReport(@RequestParam ReportType reportType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        switch (reportType) {
            case WEEKLY:
                startDate = now.minusWeeks(1);
                break;
            case MONTHLY:
                startDate = now.minusMonths(1);
                break;
            case YEARLY:
                startDate = now.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid report type");
        }
        return transactionService.generateReport(startDate, now);
    }
}
