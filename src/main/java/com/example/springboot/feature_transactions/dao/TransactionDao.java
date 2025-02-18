package com.example.springboot.feature_transactions.dao;

import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.repo.TransactionRepo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDao {
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionDao(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    /**
     * interacts with repo to find transaction by id
     *
     * @param id
     * @return
     */
    public Transaction findById(String id) {
        return transactionRepo.findById(id);
    }

    /**
     * interacts with the repo to save a transaction
     *
     * @param transaction
     * @return
     */
    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    /**
     * interacts with the repo to find the transactions between two dates
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Transaction> findTransactionsBetween(
            LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepo.findTransactionsBetween(startDate, endDate);
    }
}
