package com.example.springboot.feature_products.dao;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import com.example.springboot.feature_products.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDao {
    private final ProductRepo productRepo;

    @Autowired
    public ProductDao(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * Interacts with the repo to find details of the product
     *
     * @param id
     * @return
     */
    public Product findByIdAndCategory(String id, Category category) {
        return productRepo.findByIdAndCategory(id, category);
    }

    /**
     * saves the product to the DB through repo
     *
     * @param product
     * @return
     */
    public Product save(Product product) {
        return productRepo.save(product);
    }
}
