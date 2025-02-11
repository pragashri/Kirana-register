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
     *
     *
     * @param product
     * @return
     */
    @PostMapping("/add")
    public Product updateProductDetails(@RequestBody Product product) {
        return productService.updateProductDetails(product);
    }

    /**
     *
     *
     * @param id
     * @return
     */
    @GetMapping("/fetch")
    public Product fetchProductDetails(
            @RequestParam String id,
            @RequestParam Category category) {
        return productService.fetchProductDetails(id, category);
    }
}
