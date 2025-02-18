package com.example.springboot.feature_report.dto;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_transactions.entities.Transaction;
import java.util.List;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for Kafka messages related to report generation. Class represents the
 * structure of the message sent via Kafka when requesting a financial report.
 */
@Data
public class KafkaMessage {

    /** The type of report to be generated */
    private ReportType reportType;

    /** List of transactions included in the report. */
    private List<Transaction> transactionList;
}
