package com.example.springboot.feature_registry.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@Document()
public class StoreRegistry {

    @Id
    private String id;  // Global ObjectId for the registry
    private double creditedAmount;
    private double debitedAmount;
    private double totalAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
