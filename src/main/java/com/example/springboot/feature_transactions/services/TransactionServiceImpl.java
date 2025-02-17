package com.example.springboot.feature_transactions.services;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import com.example.springboot.feature_products.repo.ProductRepo;
import com.example.springboot.feature_registry.entities.StoreRegistry;
import com.example.springboot.feature_registry.repo.StoreRegistryRepo;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.enums.TransactionType;
import com.example.springboot.feature_transactions.helpers.TransactionHelper;
import com.example.springboot.feature_transactions.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static com.example.springboot.feature_transactions.constants.TransactionsConstants.INR_CURRENCY;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final CacheService cacheService;
    private final StoreRegistryRepo storeRegistryRepo;
    private final ProductRepo productRepo;
    private final TransactionHelper transactionHelper;
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao, CacheService cacheService, StoreRegistryRepo storeRegistryRepo, ProductRepo productRepo, TransactionHelper transactionHelper, TransactionRepo transactionRepo) {
        this.transactionDao = transactionDao;
        this.cacheService = cacheService;
        this.storeRegistryRepo = storeRegistryRepo;
        this.productRepo = productRepo;
        this.transactionHelper = transactionHelper;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public Transaction getTransactionById(String id) {
        return transactionDao.findById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public Transaction createCreditTransaction(Transaction transaction) {
        return processTransaction(transaction, TransactionType.CREDIT);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Transaction createDebitTransaction(Transaction transaction) {
        return processTransaction(transaction, TransactionType.DEBIT);
    }

    private Transaction processTransaction(Transaction transaction, TransactionType transactionType) {
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

        if (transactionType == TransactionType.CREDIT) {
            product.setQuantity(product.getQuantity() + transaction.getQuantity());
            registry = transactionHelper.updateRegistryWhenCreditTransaction(registry, transaction);
        } else if (transactionType == TransactionType.DEBIT) {
            if (product.getQuantity() < transaction.getQuantity()) {
                throw new RuntimeException("Not enough stock available!");
            }
            product.setQuantity(product.getQuantity() - transaction.getQuantity());
            registry = transactionHelper.updateRegistryWhenDebitTransaction(registry, transaction);
        }

        Transaction savedTransaction = transactionRepo.save(transaction);
        productRepo.save(product);
        storeRegistryRepo.save(registry);

        cacheService.evictReportCache();

        return savedTransaction;
    }
}
