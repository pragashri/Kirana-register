package com.example.springboot.feature_transactions.utils;

import com.example.springboot.feature_transactions.entities.Transaction;

public class TransactionUtils {

    /**
     *
     *
     * @param quantity
     * @param productPrice
     * @param transaction
     * @return
     */
    public static Transaction updateTransactionAmountDetails(
            int quantity, double productPrice, Transaction transaction) {
        int totalAmount = (int) (productPrice * quantity);

        transaction.setSum(totalAmount);
        transaction.setAmountInINR(totalAmount);

        return transaction;
    }
}
