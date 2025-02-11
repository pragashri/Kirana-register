package com.example.springboot.feature_transactions.dao;

import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionDao {
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionDao(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Transaction findById(String id) {
        return transactionRepo.findById(id);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    public List<Transaction> findTransactionsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepo.findTransactionsBetween(startDate, endDate);
    }
}
