package com.example.springboot.feature_report.kafka;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.dto.KafkaMessage;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportServiceImpl;
import com.example.springboot.feature_report.utils.ReportUtils;
import com.example.springboot.feature_transactions.entities.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService {

    private final CacheService cacheService;

    public KafkaConsumerService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @KafkaListener(topics = "generate-report", groupId = "report_group", containerFactory = "kafkaListenerContainerFactory")
    public void listenReportRequest(KafkaMessage kafkaMessage) {
        System.out.println("Received Kafka message: " + kafkaMessage);

        // Get the report type and list of transactions from the Kafka message
        ReportType reportType = kafkaMessage.getReportType();
        List<Transaction> transactions = kafkaMessage.getTransactionList();

        // Calculate the report using the provided transactions
        Report report = ReportUtils.finalizeReport(transactions);

        // Log the generated report
        System.out.println("Generated report for " + reportType + ": " + report);

        // Cache the report for future retrieval using the report type as the cache key
        String cacheKey = "report_" + reportType.name();

//        cacheService.evict(cacheKey);
        cacheService.put(cacheKey, report, 3600);
        System.out.println("Cached report with key: " + cacheKey);
    }
}
