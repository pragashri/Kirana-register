package com.example.springboot.feature_products.services;

import com.example.springboot.feature_products.dao.ProductDao;
import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * implementation for updating the details of a product through dao
     *
     * @param product
     * @return
     */
    @Override
    public Product updateProductDetails(Product product) {
        if (product == null) {
            return null;
        }

        return productDao.save(product);
    }

    /**
     * implementation of fetching product detail through dao
     *
     * @param id
     * @return
     */
    @Override
    public Product fetchProductDetails(String id, Category category) {
        if (id == null || category == null) {
            return null;
        }

        return productDao.findByIdAndCategory(id, category);
    }
}
