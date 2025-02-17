package com.example.springboot.feature_transactions.controller;

import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/credit")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Transaction> createCreditTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createCreditTransaction(transaction));
    }


    @PostMapping("/debit")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Transaction> createDebitTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createDebitTransaction(transaction));
    }


    @GetMapping("/fetch")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Transaction> getTransactionById(@RequestParam String id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
