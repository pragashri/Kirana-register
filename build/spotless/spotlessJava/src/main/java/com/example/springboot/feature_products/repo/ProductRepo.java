package com.example.springboot.feature_products.repo;

import com.example.springboot.feature_products.entities.Product;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<Product, String> {

    /**
     * This method directly interacts with the MongoDB to find product details by ID.
     *
     * @param id The unique identifier of the product.
     * @return The product details.
     */
    Optional<Product> findById(String id);
}
