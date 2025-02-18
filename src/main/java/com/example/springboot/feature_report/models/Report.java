package com.example.springboot.feature_report.models;

import java.io.Serializable;
import lombok.Data;

/**
 * Model representing a financial report containing credit, debit, and total amounts. Implements
 * Serializable to allow caching and efficient data transfer.
 */
@Data
public class Report implements Serializable {
    private double creditAmount;
    private double debitAmount;
    private double totalAmount;
}
