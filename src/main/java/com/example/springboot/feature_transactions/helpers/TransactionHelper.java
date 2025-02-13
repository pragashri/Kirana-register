package com.example.springboot.feature_transactions.helpers;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_registry.entities.StoreRegistry;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.utils.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static com.example.springboot.feature_transactions.constants.TransactionsConstants.APIURL;
import static com.example.springboot.feature_transactions.constants.TransactionsConstants.INR_CURRENCY;

@Service
public class TransactionHelper {

    private final CacheService cacheService;
    private final RestTemplate restTemplate;

    @Autowired
    public TransactionHelper(CacheService cacheService, RestTemplate restTemplate) {
        this.cacheService = cacheService;
        this.restTemplate = restTemplate;
    }

    /**
     * helper method to update the registry when credit occurs
     *
     * @param registry
     * @param transaction
     * @return
     */
    public StoreRegistry updateRegistryWhenCreditTransaction(StoreRegistry registry, Transaction transaction) {
        if (registry == null || transaction == null) {
            return null;
        }

        double totalCreditedAmount = registry.getCreditedAmount() + transaction.getAmountInINR();
        double totalAmount = registry.getTotalAmount() + transaction.getAmountInINR();

        registry.setCreditedAmount(totalCreditedAmount);
        registry.setTotalAmount(totalAmount);

        return registry;
    }


    /**
     * helper method to update the registry when debit occurs
     *
     * @param registry
     * @param transaction
     * @return
     */
    public StoreRegistry updateRegistryWhenDebitTransaction(StoreRegistry registry, Transaction transaction) {
        if (registry == null || transaction == null) {
            return null;
        }

        double totalDebitedAmount = registry.getDebitedAmount() + transaction.getAmountInINR();
        double totalAmount = registry.getTotalAmount() - transaction.getAmountInINR();

        registry.setDebitedAmount(totalDebitedAmount);
        registry.setTotalAmount(totalAmount);

        return registry;
    }

    /**
     * Converts the given amount to INR if it's in a different currency.
     * Uses an external API for conversion rates.
     *
     * @param amount   The amount to convert.
     * @param currency The current currency of the amount.
     * @return The converted amount in INR.
     */
    /**
     * Converts the given amount to INR if it's in a different currency.
     * Uses an external API for conversion rates, with caching in Redis.
     *
     * @param amount   The amount to convert.
     * @param currency The current currency of the amount.
     * @return The converted amount in INR.
     */
    public double convertToINR(double amount, String currency) {
        if (INR_CURRENCY.equalsIgnoreCase(currency)) {
            return TransactionUtils.roundToTwoDecimalPlaces(amount);
        }

        String cacheKey = "currency_" + currency + "_INR";
        Double cachedRate = cacheService.get(cacheKey, Double.class);

        if (cachedRate != null) {
            System.out.println("Fetching exchange rate from cache...");
            return TransactionUtils.roundToTwoDecimalPlaces(amount * cachedRate);
        }

        try {
            System.out.println("Fetching exchange rate from API...");
            ResponseEntity<Map> response = restTemplate.getForEntity(APIURL, Map.class);

            if (response.getBody() != null && (boolean) response.getBody().get("success")) {
                Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
                double exchangeRate = rates.get(INR_CURRENCY);

                // Store the exchange rate in cache with 1-hour expiry
                cacheService.put(cacheKey, exchangeRate, 3600);

                return TransactionUtils.roundToTwoDecimalPlaces(amount * exchangeRate);
            }
        } catch (Exception e) {
            throw new RuntimeException("Currency conversion failed: " + e.getMessage());
        }

        return amount;
    }


}

