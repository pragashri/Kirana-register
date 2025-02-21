package com.example.springboot.feature_products.services;

import com.example.springboot.feature_products.dao.ProductDao;
import com.example.springboot.feature_products.entities.Product;
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
     * Implementation of fetching product details by ID.
     *
     * @param id The unique identifier of the product.
     * @return The product details.
     */
    @Override
    public Product fetchProductDetails(String id) {
        if (id == null) {
            return null;
        }
        return productDao.findById(id);
    }
}
