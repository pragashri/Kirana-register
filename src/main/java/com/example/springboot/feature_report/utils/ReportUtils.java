package com.example.springboot.feature_report.utils;

import com.example.springboot.feature_products.dao.ProductDao;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.enums.TransactionType;
import com.example.springboot.feature_transactions.helpers.TransactionHelper;
import com.example.springboot.feature_transactions.utils.TransactionUtils;

import java.util.List;

public class ReportUtils {

    /**
     * Processes a list of transactions to compute the report totals.
     *
     * @param transactions List of transactions
     * @return Report object containing calculated totals
     */
    public static Report finalizeReport(List<Transaction> transactions) {
        double totalAmount = 0;
        double creditedAmount = 0;
        double debitedAmount = 0;

        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmountInINR();
            if (transaction.getTransactionType() == TransactionType.CREDIT) {
                creditedAmount += transaction.getAmountInINR();
            } else if (transaction.getTransactionType() == TransactionType.DEBIT) {
                debitedAmount += transaction.getAmountInINR();
            }
        }

        Report report = new Report();
        report.setCreditAmount(TransactionUtils.roundToTwoDecimalPlaces(creditedAmount));
        report.setDebitAmount(TransactionUtils.roundToTwoDecimalPlaces(debitedAmount));
        report.setTotalAmount(TransactionUtils.roundToTwoDecimalPlaces(totalAmount));

        return report;
    }

}
