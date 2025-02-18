package com.example.springboot.feature_transactions.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionUtils {

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
