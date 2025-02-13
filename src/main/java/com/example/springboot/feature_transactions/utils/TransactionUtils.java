package com.example.springboot.feature_transactions.utils;

import com.example.springboot.feature_transactions.entities.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    /**
     * rounds off to two decimal places
     *
     * @param value
     * @return
     */
    public static double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
