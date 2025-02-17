package com.example.springboot.feature_report.dto;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_transactions.entities.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class KafkaMessage {
    private ReportType reportType;
    private List<Transaction> transactionList;
}

