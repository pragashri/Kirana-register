package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_transactions.entities.Transaction;

public interface TransactionService {

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id The ID of the transaction to retrieve.
     * @return The {@link Transaction} entity corresponding to the given ID.
     */
    Transaction getTransactionById(String id);

    /**
     * Creates a new credit transaction.
     *
     * @param transaction The transaction details to create.
     * @return The created {@link Transaction} entity.
     */
    Transaction createCreditTransaction(Transaction transaction);

    /**
     * Creates a new debit transaction.
     *
     * @param transaction The transaction details to create.
     * @return The created {@link Transaction} entity.
     */
    Transaction createDebitTransaction(Transaction transaction);
}
