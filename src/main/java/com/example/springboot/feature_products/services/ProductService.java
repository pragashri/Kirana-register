package com.example.springboot.feature_products.services;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;

public interface ProductService {

    /**
     *
     *
     * @param product
     * @return
     */
    Product updateProductDetails(Product product);

    /**
     *
     *
     * @param id
     * @param category
     * @return
     */
    Product fetchProductDetails(String id, Category category);
}
