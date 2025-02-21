package com.example.springboot.feature_transactions.services;

import static com.example.springboot.feature_transactions.constants.TransactionsConstants.INR_CURRENCY;
import static com.example.springboot.feature_transactions.constants.TransactionLogConstants.NOT_ENOUGH_STOCK;
import static com.example.springboot.feature_transactions.constants.TransactionLogConstants.PRODUCT_NOT_FOUND;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.repo.ProductRepo;
import com.example.springboot.feature_registry.entities.StoreRegistry;
import com.example.springboot.feature_registry.repo.StoreRegistryRepo;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_transactions.enums.TransactionType;
import com.example.springboot.feature_transactions.helpers.TransactionHelper;
import com.example.springboot.feature_transactions.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final CacheService cacheService;
    private final StoreRegistryRepo storeRegistryRepo;
    private final ProductRepo productRepo;
    private final TransactionHelper transactionHelper;
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionServiceImpl(
            TransactionDao transactionDao,
            CacheService cacheService,
            StoreRegistryRepo storeRegistryRepo,
            ProductRepo productRepo,
            TransactionHelper transactionHelper,
            TransactionRepo transactionRepo) {
        this.transactionDao = transactionDao;
        this.cacheService = cacheService;
        this.storeRegistryRepo = storeRegistryRepo;
        this.productRepo = productRepo;
        this.transactionHelper = transactionHelper;
        this.transactionRepo = transactionRepo;
    }

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id The ID of the transaction to retrieve.
     * @return The transaction corresponding to the given ID.
     */
    @Override
    public Transaction getTransactionById(String id) {
        return transactionDao.findById(id);
    }

    /**
     * Creates a new credit transaction. Validates the transaction, updates product stock, and
     * updates the store registry accordingly.
     *
     * @param transaction The transaction details to create.
     * @return The created credit transaction.
     */
    @Override
//    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public Transaction createCreditTransaction(Transaction transaction) {
        return processTransaction(transaction, TransactionType.CREDIT);
    }

    /**
     * Creates a new debit transaction. Validates the transaction, ensures sufficient stock, and
     * updates the store registry accordingly.
     *
     * @param transaction The transaction details to create.
     * @return The created debit transaction.
     */
    @Override
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Transaction createDebitTransaction(Transaction transaction) {
        return processTransaction(transaction, TransactionType.DEBIT);
    }

    /**
     * Processes a transaction (either credit or debit). Updates product stock, store registry, and
     * handles currency conversion if necessary. Also ensures proper validation and caching.
     *
     * @param transaction The transaction details.
     * @param transactionType The type of the transaction (CREDIT or DEBIT).
     * @return The processed transaction.
     */
    private Transaction processTransaction(
            Transaction transaction, TransactionType transactionType) {
        StoreRegistry registry = storeRegistryRepo.findFirstByOrderByIdDesc();
        if (registry == null) {
            registry = new StoreRegistry();
        }

        Product product = productRepo.findById(transaction.getProductId()).orElse(null);
        if (product == null) {
            throw new RuntimeException(PRODUCT_NOT_FOUND);
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
                throw new RuntimeException(NOT_ENOUGH_STOCK);
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
