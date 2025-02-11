package com.example.springboot.service;

import com.example.springboot.model.Product;
import com.example.springboot.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(new ObjectId().toHexString());  // Convert ObjectId to String
        }
        return productRepository.save(product);
    }


    public Optional<Product> getProductById(ObjectId id) {
        return productRepository.findById(id);
    }
}
