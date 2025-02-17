package com.example.springboot.feature_report.services;

import com.example.springboot.feature_report.dto.KafkaMessage;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.kafka.KafkaProducerService;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_transactions.entities.Transaction;
import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.models.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final TransactionDao transactionDao;
    private final CacheService cacheService;
    private final KafkaProducerService kafkaProducerService;

    public ReportServiceImpl(TransactionDao transactionDao, CacheService cacheService, KafkaProducerService kafkaProducerService) {
        this.transactionDao = transactionDao;
        this.cacheService = cacheService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public Report generateReport(ReportType reportType) {
        String cacheKey = "report_" + reportType.name();
        Report cachedReport = cacheService.get(cacheKey, Report.class);

        if (cachedReport != null) {
            log.info("Fetching report from cache...");
            return cachedReport;
        } else {
            log.info("Fetching report from DB...");

            LocalDateTime endDate = LocalDateTime.now();   // Current date
            LocalDateTime startDate = getStartDateBasedOnType(reportType);

            List<Transaction> transactions = transactionDao.findTransactionsBetween(startDate, endDate);

            if (transactions == null || transactions.isEmpty()) {
                log.warn("No transactions found for the given period.");
                return null;  // Or return an empty report
            }

            log.info("Fetched {} transactions from DB", transactions.size());

            KafkaMessage kafkaMessage = new KafkaMessage();
            kafkaMessage.setReportType(reportType);
            kafkaMessage.setTransactionList(transactions);

            kafkaProducerService.sendReportRequest(kafkaMessage);  // Sending the report request as DTO


        }
        return cachedReport;
    }

    private LocalDateTime getStartDateBasedOnType(ReportType reportType) {
        LocalDateTime currentDate = LocalDateTime.now();
        return switch (reportType) {
            case WEEKLY -> currentDate.minusWeeks(1);
            case MONTHLY -> currentDate.minusMonths(1);
            case YEARLY -> currentDate.minusYears(1);
        };
    }
}
