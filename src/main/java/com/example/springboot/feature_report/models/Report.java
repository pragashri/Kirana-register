package com.example.springboot.feature_report.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Report implements Serializable {
    private double creditAmount;
    private double debitAmount;
    private double totalAmount;
}
