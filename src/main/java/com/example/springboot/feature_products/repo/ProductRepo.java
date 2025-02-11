package com.example.springboot.feature_products.repo;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<Product, String> {

    /**
     *
     *
     * @param id
     * @return
     */
    Product findByIdAndCategory(String id, Category category);
}
