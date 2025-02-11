package com.example.springboot.feature_transactions.entities;

import com.example.springboot.feature_transactions.enums.TransactionType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transaction")
public class Transaction {

    @Id private String id;
    private String employeeId;
    private String ownerId;
    private String productId;
    private int quantity;
    private double sum;
    private String currency;
    private double amountInINR;
    private double amountInUSD;
    private String transactionStatus;
    private TransactionType transactionType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
