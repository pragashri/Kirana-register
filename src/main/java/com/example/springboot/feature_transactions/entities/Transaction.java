package com.example.springboot.feature_transactions.entities;

import com.example.springboot.feature_transactions.enums.TransactionStatusType;
import com.example.springboot.feature_transactions.enums.TransactionType;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document()
public class Transaction {

    @Id private String id;

    private String productId;
    private int quantity;
    private double sum;
    private String currency;
    private double amountInINR;
    private TransactionStatusType transactionStatus;
    private TransactionType transactionType;

    @CreatedDate private LocalDateTime createdAt;

    @LastModifiedDate private LocalDateTime updatedAt;
}
