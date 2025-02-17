package com.example.springboot.feature_transactions.controller;

import com.example.springboot.feature_report.dto.ReportRequestDto;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportService;
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
    private final ReportService reportService;

    @Autowired
    public TransactionController(TransactionService transactionService, ReportService reportService) {
        this.transactionService = transactionService;
        this.reportService = reportService;
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

}
