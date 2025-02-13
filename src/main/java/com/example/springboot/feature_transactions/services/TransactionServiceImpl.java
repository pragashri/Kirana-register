package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.utils.ReportUtils;
import com.example.springboot.feature_transactions.enums.TransactionType;
import com.example.springboot.feature_transactions.helpers.TransactionHelper;
import com.example.springboot.feature_transactions.repo.TransactionRepo;
import com.example.springboot.feature_registry.entities.StoreRegistry;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_products.repo.ProductRepo;
import com.example.springboot.feature_registry.repo.StoreRegistryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

import static com.example.springboot.feature_transactions.constants.TransactionsConstants.INR_CURRENCY;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final TransactionRepo transactionRepo;
    private final ProductRepo productRepo;
    private final TransactionHelper transactionHelper;
    private final StoreRegistryRepo storeRegistryRepo;
    private final CacheService cacheService;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao, TransactionRepo transactionRepo, ProductRepo productRepo,
                                  TransactionHelper transactionHelper, StoreRegistryRepo storeRegistryRepo, CacheService cacheService) {
        this.transactionDao = transactionDao;
        this.transactionRepo = transactionRepo;
        this.productRepo = productRepo;
        this.transactionHelper = transactionHelper;
        this.storeRegistryRepo = storeRegistryRepo;
        this.cacheService = cacheService;
    }

    /**
     *
     * find transaction by id
     * @param id
     * @return
     */
    @Override
    public Transaction getTransactionById(String id) {
        return transactionDao.findById(id);
    }

    /**
     * add a new transaction
     * helper class helps update the registry
     * if not in inr, convertToINR in helper is called
     * when a transaction is saved, the cache keys are evicted
     *
     * @param transaction
     * @return
     */
    @Override
    public Transaction updateTransactionDetails(Transaction transaction) {
        StoreRegistry registry = storeRegistryRepo.findFirstByOrderByIdDesc();
        if (registry == null) {
            registry = new StoreRegistry();
        }

        Product product = productRepo.findByIdAndCategory(transaction.getProductId(), Category.GROCERY);
        if (product == null) {
            throw new RuntimeException("Product not found!");
        }

        double sum = transaction.getQuantity() * product.getPrice();
        transaction.setSum(sum);

        if (!INR_CURRENCY.equals(transaction.getCurrency())) {
            double convertedPrice = transactionHelper.convertToINR(sum, transaction.getCurrency());
            transaction.setAmountInINR(convertedPrice);
        } else {
            transaction.setAmountInINR(sum);
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

        Transaction savedTransaction = transactionRepo.save(transaction);
        productRepo.save(product);
        storeRegistryRepo.save(registry);

        // 🔥 Evict cached reports when a new transaction is made
        cacheService.evictReportCache();

        return savedTransaction;
    }

    /**
     *
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Report generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        System.out.println("Fetching report from DB..."); // Debugging line to confirm caching
        List<Transaction> transactions = transactionDao.findTransactionsBetween(startDate, endDate);
        return ReportUtils.finalizeReport(transactions); // Using ReportUtils for final calculations
    }


}
