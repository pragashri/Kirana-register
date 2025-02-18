package com.example.springboot.feature_products.repo;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepo extends MongoRepository<Product, String> {

    /**
     * This method directly interacts with the MongoDB to find product details by ID.
     *
     * @param id The unique identifier of the product.
     * @return The product details.
     */
    Optional<Product> findById(String id);

}
