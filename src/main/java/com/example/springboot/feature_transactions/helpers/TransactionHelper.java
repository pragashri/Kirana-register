package com.example.springboot.feature_transactions.helpers;

import com.example.springboot.model.StoreRegistry;
import com.example.springboot.feature_transactions.entities.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionHelper {

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
}

