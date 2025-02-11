package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import com.example.springboot.feature_transactions.enums.TransactionType;
import com.example.springboot.feature_transactions.helpers.TransactionHelper;
import com.example.springboot.feature_transactions.repo.TransactionRepo;
import com.example.springboot.feature_transactions.utils.TransactionUtils;
import com.example.springboot.model.StoreRegistry;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_products.repo.ProductRepo;
import com.example.springboot.repository.KiranaRegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.springboot.feature_transactions.constants.TransactionsConstants.INR_CURRENCY;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final TransactionRepo transactionRepo;
    private final ProductRepo productRepo;
    private final TransactionHelper transactionHelper;
    private final KiranaRegistryRepository kiranaRegistryRepository;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao, TransactionRepo transactionRepo, ProductRepo productRepo,
                                  TransactionHelper transactionHelper, KiranaRegistryRepository kiranaRegistryRepository) {
        this.transactionDao = transactionDao;
        this.transactionRepo = transactionRepo;
        this.productRepo = productRepo;
        this.transactionHelper = transactionHelper;
        this.kiranaRegistryRepository = kiranaRegistryRepository;
    }

    @Override
    public Transaction getTransactionById(String id) {
        return transactionDao.findById(id);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        StoreRegistry registry = kiranaRegistryRepository.findFirstByOrderByIdDesc();

        if (registry == null) {
            registry = new StoreRegistry();
            kiranaRegistryRepository.save(registry);
        }

        Product product = productRepo.findByIdAndCategory(transaction.getProductId(), Category.GROCERY);
        if (product == null) {
            throw new RuntimeException("Product not found!");
        }

        if (transaction.getTransactionType() == TransactionType.CREDIT) {
            product.setQuantity(product.getQuantity() + transaction.getQuantity());
            registry = transactionHelper.updateRegistryWhenCreditTransaction(registry, transaction);
        } else if (transaction.getTransactionType() == TransactionType.DEBIT) {
            if (product.getQuantity() < transaction.getQuantity()) {
                throw new RuntimeException("Not enough stock available!");
            }
            product.setQuantity(product.getQuantity() - transaction.getQuantity());
            registry = transactionHelper.updateRegistryWhenDebitTransaction(registry, transaction);
        }

        productRepo.save(product);

        double sum = transaction.getQuantity() * product.getPrice();
        transaction.setSum(sum);

        if (!INR_CURRENCY.equals(transaction.getCurrency())) {
            double convertedPrice = convertToINR(sum, transaction.getCurrency());
            transaction.setAmountInINR(convertedPrice);
        } else {
            transaction.setAmountInINR(sum);
        }

        transaction = transactionRepo.save(transaction);
        kiranaRegistryRepository.save(registry);

        return transaction;
    }

    private double convertToINR(double amount, String currency) {
        if (INR_CURRENCY.equalsIgnoreCase(currency)) {
            return amount;
        }

        try {
            String apiUrl = "https://api.fxratesapi.com/latest";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

            if (response.getBody() != null && (boolean) response.getBody().get("success")) {
                Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
                double usdToInrRate = rates.get(INR_CURRENCY);
                return amount * usdToInrRate;
            }
        } catch (Exception e) {
            throw new RuntimeException("Currency conversion failed: " + e.getMessage());
        }

        return amount;
    }

    @Override
    public Map<String, Double> generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionDao.findTransactionsBetween(startDate, endDate);
        double totalAmount = 0, totalCredits = 0, totalDebits = 0, creditedAmount = 0, debitedAmount = 0;
        double creditCount = 0, debitCount = 0;

        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmountInINR();
            if (transaction.getTransactionType() == TransactionType.CREDIT) {
                totalCredits += transaction.getSum();
                creditedAmount += transaction.getAmountInINR();
                creditCount++;
            } else if (transaction.getTransactionType() == TransactionType.DEBIT) {
                totalDebits += transaction.getSum();
                debitedAmount += transaction.getAmountInINR();
                debitCount++;
            }
        }

        Map<String, Double> report = new HashMap<>();
        report.put("Net Current Amount" , creditedAmount - debitedAmount);
        report.put("Total Transactions Amount", totalAmount);
        report.put("Total Credits", creditCount);
        report.put("Total Debits", debitCount);
        report.put("Total Credited Amount", creditedAmount);
        report.put("Total Debited Amount", debitedAmount);
        return report;
    }
}
