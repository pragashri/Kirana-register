package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportCacheService;
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
                                  TransactionHelper transactionHelper, StoreRegistryRepo storeRegistryRepo, @Lazy ReportCacheService reportCacheService1, CacheService cacheService) {
        this.transactionDao = transactionDao;
        this.transactionRepo = transactionRepo;
        this.productRepo = productRepo;
        this.transactionHelper = transactionHelper;
        this.storeRegistryRepo = storeRegistryRepo;
        this.cacheService = cacheService;
    }

    @Override
    public Transaction getTransactionById(String id) {
        return transactionDao.findById(id);
    }

    @Override
//    @CacheEvict(value = "reports", allEntries = true)

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

        // Use helper class for INR conversion
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
        cacheService.evict("report_weekly");
        cacheService.evict("report_monthly");
        cacheService.evict("report_yearly");

        regenerateReportsCache();

        return savedTransaction;
    }

    @Override
//    @Cacheable(value = "reports", key = "#startDate.toString().concat('-').concat(#endDate.toString())")

    public Report generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        System.out.println("Fetching report from DB..."); // Debugging line to confirm caching
        List<Transaction> transactions = transactionDao.findTransactionsBetween(startDate, endDate);
        return ReportUtils.finalizeReport(transactions); // Using ReportUtils for final calculations
    }

    private void regenerateReportsCache() {
        LocalDateTime now = LocalDateTime.now();

        // Weekly Report
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        Report weeklyReport = generateReport(oneWeekAgo, now);
        cacheService.put("report_weekly", weeklyReport, 3600);
        System.out.println("✅ Cached Weekly Report: " + weeklyReport);

        // Monthly Report
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        Report monthlyReport = generateReport(oneMonthAgo, now);
        cacheService.put("report_monthly", monthlyReport, 3600);
        System.out.println("✅ Cached Monthly Report: " + monthlyReport);

        // Yearly Report
        LocalDateTime oneYearAgo = now.minusYears(1);
        Report yearlyReport = generateReport(oneYearAgo, now);
        cacheService.put("report_yearly", yearlyReport, 3600);
        System.out.println("✅ Cached Yearly Report: " + yearlyReport);
    }

}
