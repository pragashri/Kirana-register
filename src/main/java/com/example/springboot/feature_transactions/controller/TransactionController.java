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

    /**
     * Endpoint to create a credit transaction. Requires the user to have the role of either USER or
     * ADMIN.
     *
     * @param transaction The transaction data to be created.
     * @return A {@link ResponseEntity} containing the created credit transaction.
     */
    @PostMapping("/credit")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Transaction> createCreditTransaction(
            @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createCreditTransaction(transaction));
    }

    /**
     * Endpoint to create a debit transaction. Requires the user to have the role of ADMIN.
     *
     * @param transaction The transaction data to be created.
     * @return A {@link ResponseEntity} containing the created debit transaction.
     */
    @PostMapping("/debit")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Transaction> createDebitTransaction(
            @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createDebitTransaction(transaction));
    }

    /**
     * Endpoint to fetch a transaction by its ID. Requires the user to have the role of either USER
     * or ADMIN.
     *
     * @param id The ID of the transaction to retrieve.
     * @return A {@link ResponseEntity} containing the transaction data.
     */
    @GetMapping("/fetch")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Transaction> getTransactionById(@RequestParam String id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
