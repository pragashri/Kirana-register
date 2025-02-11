package com.example.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

@Document(collection = "kirana_registry")
public class KiranaRegistry {

    @Id
    private ObjectId id;  // Global ObjectId for the registry
    private double creditedAmount;
    private double debitedAmount;
    private double totalAmount;  // This will track the total balance

    // Default constructor
    public KiranaRegistry() {
        this.creditedAmount = 0.0;
        this.debitedAmount = 0.0;
        this.totalAmount = 0.0;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public double getCreditedAmount() {
        return creditedAmount;
    }

    public void setCreditedAmount(double creditedAmount) {
        this.creditedAmount = creditedAmount;
    }

    public double getDebitedAmount() {
        return debitedAmount;
    }

    public void setDebitedAmount(double debitedAmount) {
        this.debitedAmount = debitedAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
