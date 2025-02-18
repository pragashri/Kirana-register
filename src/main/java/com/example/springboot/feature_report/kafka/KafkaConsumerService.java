package com.example.springboot.feature_report.kafka;

import static com.example.springboot.feature_report.constants.ReportConstants.*;
import static com.example.springboot.feature_report.logConstants.LogConstants.*;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.dto.KafkaMessage;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.utils.ReportUtils;
import com.example.springboot.feature_transactions.entities.Transaction;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service responsible for consuming Kafka messages related to report generation. This class listens
 * for messages on the "generate-report" topic, processes transaction data, generates reports, and
 * caches them for future use.
 */
@Service
@Slf4j
public class KafkaConsumerService {

    private final CacheService cacheService;

    /**
     * Constructor-based dependency injection for CacheService.
     *
     * @param cacheService Service responsible for caching reports.
     */
    public KafkaConsumerService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * Kafka listener that consumes messages from the "generate-report" topic. Extracts the report
     * type and transaction list from the Kafka message, generates a financial report, and caches it
     * for future retrieval.
     *
     * @param kafkaMessage The Kafka message containing report type and transaction data.
     */
    @KafkaListener(
            topics = "generate-report",
            groupId = "report_group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenReportRequest(KafkaMessage kafkaMessage) {
        log.info(RECIEVED_MESSAGE + kafkaMessage);

        ReportType reportType = kafkaMessage.getReportType();
        List<Transaction> transactions = kafkaMessage.getTransactionList();

        Report report = ReportUtils.finalizeReport(transactions);

        log.info(GENERATED_REPORT + reportType + ": " + report);

        String cacheKey = KEY + reportType.name();

        cacheService.put(cacheKey, report, TTL);
        log.info(CACHED_REPORT + cacheKey);
    }
}
