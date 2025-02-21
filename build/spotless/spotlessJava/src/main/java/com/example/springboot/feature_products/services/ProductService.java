package com.example.springboot.feature_products.services;

import com.example.springboot.feature_products.entities.Product;

public interface ProductService {

    /**
     * interface for adding product details
     *
     * @param product
     * @return
     */
    Product updateProductDetails(Product product);

    /**
     * Interface for fetching product details by ID.
     *
     * @param id The unique identifier of the product.
     * @return The product details.
     */
    Product fetchProductDetails(String id);
}
