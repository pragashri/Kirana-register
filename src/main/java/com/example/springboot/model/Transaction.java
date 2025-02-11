package com.example.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

@Document(collection = "transaction")
public class Transaction {

    @Id
    private ObjectId id;
    private ObjectId employeeId;
    private ObjectId ownerId;
    private ObjectId productId;
    private int quantity;
    private double sum;
    private String currency;
    private String transactionStatus;
    private String transactionType; // CREDIT/DEBIT

    // Constructors
    public Transaction() {
        this.id = new ObjectId();
    }

    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getEmployeeId() { return employeeId; }
    public void setEmployeeId(ObjectId employeeId) { this.employeeId = employeeId; }

    public ObjectId getOwnerId() { return ownerId; }
    public void setOwnerId(ObjectId ownerId) { this.ownerId = ownerId; }

    public ObjectId getProductId() { return productId; }
    public void setProductId(ObjectId productId) { this.productId = productId; }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSum() { return sum; }
    public void setSum(double sum) { this.sum = sum; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTransactionStatus() { return transactionStatus; }
    public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}
