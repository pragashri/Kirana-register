package com.example.springboot.feature_products.controllers;

import com.example.springboot.feature_products.entities.Product;
import com.example.springboot.feature_products.enums.Category;
import com.example.springboot.feature_products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Product details are added to the DB
     *
     * @param product
     * @return
     */
    @PostMapping("/add")
    public Product updateProductDetails(@RequestBody Product product) {
        return productService.updateProductDetails(product);
    }

    /**
     * Fetches product details using the product ID.
     *
     * @param id The unique identifier of the product.
     * @return The product details.
     */
    @GetMapping("/fetch")
    public Product fetchProductDetails(@RequestParam String id) {
        return productService.fetchProductDetails(id);
    }
}
