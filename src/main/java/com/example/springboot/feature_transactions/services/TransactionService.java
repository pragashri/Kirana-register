package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_transactions.entities.Transaction;

public interface TransactionService {

    Transaction getTransactionById(String id);

    Transaction createCreditTransaction(Transaction transaction);

    Transaction createDebitTransaction(Transaction transaction);
}
