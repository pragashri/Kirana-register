package com.example.springboot.feature_products.services;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;

public interface ProductService {

    /**
     * interface for adding product details
     *
     * @param product
     * @return
     */
    Product updateProductDetails(Product product);

    /**
     * interface for fetching product details
     *
     * @param id
     * @param category
     * @return
     */
    Product fetchProductDetails(String id, Category category);
}
