package com.example.springboot.feature_products.entities;

import com.example.springboot.feature_products.enums.Category;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document()
public class Product {

    @Id private String id;
    private String productName;
    private Category category;
    private int quantity;
    private double price;

    @CreatedDate private LocalDateTime createdAt;

    @LastModifiedDate private LocalDateTime updatedAt;
}
