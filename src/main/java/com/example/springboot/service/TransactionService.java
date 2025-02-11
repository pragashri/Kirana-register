package com.example.springboot.service;

import com.example.springboot.model.Transaction;
import com.example.springboot.model.Product;
import com.example.springboot.model.KiranaRegistry;
import com.example.springboot.repository.TransactionRepository;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.repository.KiranaRegistryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KiranaRegistryRepository kiranaRegistryRepository;

    public Transaction createTransaction(Transaction transaction) {
        // Retrieve the global KiranaRegistry
        KiranaRegistry registry = kiranaRegistryRepository.findFirstByOrderByIdDesc();

        if (registry == null) {
            registry = new KiranaRegistry();
            kiranaRegistryRepository.save(registry);
        }

        // Fetch the product being transacted
        ObjectId productId = transaction.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new RuntimeException("Product not found"));

        // Check transaction type and update product quantity
        if ("CREDIT".equals(transaction.getTransactionType())) {
            product.setQuantity(product.getQuantity() + transaction.getQuantity());
            registry.setCreditedAmount(registry.getCreditedAmount() + transaction.getSum());
            registry.setTotalAmount(registry.getTotalAmount() + transaction.getSum());
        } else if ("DEBIT".equals(transaction.getTransactionType())) {
            if (product.getQuantity() < transaction.getQuantity()) {
                throw new RuntimeException("Not enough stock available!");
            }
            product.setQuantity(product.getQuantity() - transaction.getQuantity());
            registry.setDebitedAmount(registry.getDebitedAmount() + transaction.getSum());
            registry.setTotalAmount(registry.getTotalAmount() - transaction.getSum());
        }

        // Save the updated product
        productRepository.save(product);

        // Save the updated registry
        kiranaRegistryRepository.save(registry);

        // Save the transaction
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransactionById(ObjectId id) {
        return transactionRepository.findById(id);
    }

}
