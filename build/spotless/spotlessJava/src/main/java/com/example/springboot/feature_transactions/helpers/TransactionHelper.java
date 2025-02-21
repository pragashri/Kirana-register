package com.example.springboot.feature_transactions.helpers;

import static com.example.springboot.feature_transactions.constants.TransactionLogConstants.*;
import static com.example.springboot.feature_transactions.constants.TransactionsConstants.*;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_registry.entities.StoreRegistry;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.utils.TransactionUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
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
    public StoreRegistry updateRegistryWhenCreditTransaction(
            StoreRegistry registry, Transaction transaction) {
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
    public StoreRegistry updateRegistryWhenDebitTransaction(
            StoreRegistry registry, Transaction transaction) {
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
     * Converts the given amount to INR if it's in a different currency. Uses an external API for
     * conversion rates, with caching in Redis.
     *
     * @param amount The amount to convert.
     * @param currency The current currency of the amount.
     * @return The converted amount in INR.
     */
    public double convertToINR(double amount, String currency) {
        if (INR_CURRENCY.equalsIgnoreCase(currency)) {
            return TransactionUtils.roundToTwoDecimalPlaces(amount);
        }

        String cacheKey = CURRENCY_KEY + currency + INR_KEY;
        Double cachedRate = cacheService.get(cacheKey, Double.class);

        if (cachedRate != null) {
            log.info(FETCH_EXCHANGE_FROM_CACHE);
            return TransactionUtils.roundToTwoDecimalPlaces(amount * cachedRate);
        }

        try {
            log.info(FETCH_EXCHANGE_FROM_API);
            ResponseEntity<Map> response = restTemplate.getForEntity(APIURL, Map.class);

            if (response.getBody() != null && (boolean) response.getBody().get("success")) {
                Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
                double exchangeRate = rates.get(INR_CURRENCY);

                cacheService.put(cacheKey, exchangeRate, 3600);

                return TransactionUtils.roundToTwoDecimalPlaces(amount * exchangeRate);
            }
        } catch (Exception e) {
            throw new RuntimeException(EXCHANGE_FAILED + e.getMessage());
        }

        return amount;
    }
}
