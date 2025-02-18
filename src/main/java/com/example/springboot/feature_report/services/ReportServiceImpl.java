package com.example.springboot.feature_report.services;

import static com.example.springboot.feature_report.constants.ReportConstants.KEY;
import static com.example.springboot.feature_report.logConstants.LogConstants.*;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.dto.KafkaMessage;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.kafka.KafkaProducerService;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_transactions.entities.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final TransactionDao transactionDao;
    private final CacheService cacheService;
    private final KafkaProducerService kafkaProducerService;

    public ReportServiceImpl(
            TransactionDao transactionDao,
            CacheService cacheService,
            KafkaProducerService kafkaProducerService) {
        this.transactionDao = transactionDao;
        this.cacheService = cacheService;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Generates a financial report based on the specified report type.
     *
     * <p>First, attempts to retrieve the report from the cache. If not found, fetches transactions
     * from the database, generates the report, and triggers Kafka processing.
     *
     * @param reportType The type of report to generate (e.g., WEEKLY, MONTHLY, YEARLY).
     * @return The generated {@link Report}, or {@code null} if no transactions were found.
     */
    @Override
    public Report generateReport(ReportType reportType) {
        String cacheKey = KEY + reportType.name();
        Report cachedReport = cacheService.get(cacheKey, Report.class);

        if (cachedReport != null) {
            log.info(FETCH_CACHE);
            return cachedReport;
        } else {
            log.info(FETCH_DB);

            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = getStartDateBasedOnType(reportType);

            List<Transaction> transactions =
                    transactionDao.findTransactionsBetween(startDate, endDate);

            if (transactions == null || transactions.isEmpty()) {
                log.warn(NO_TRANSACTION_FOUND);
                return null;
            }

            log.info(TRANSACTIONS_FOUND_FROM_DB, transactions.size());

            KafkaMessage kafkaMessage = new KafkaMessage();
            kafkaMessage.setReportType(reportType);
            kafkaMessage.setTransactionList(transactions);

            kafkaProducerService.sendReportRequest(
                    kafkaMessage); // Sending the report request as DTO
        }
        return cachedReport;
    }

    /**
     * Returns the start date for generating a report based on the report type.
     *
     * @param reportType The type of report (e.g., WEEKLY, MONTHLY, YEARLY).
     * @return The start date for the report period.
     */
    private LocalDateTime getStartDateBasedOnType(ReportType reportType) {
        LocalDateTime currentDate = LocalDateTime.now();
        return switch (reportType) {
            case WEEKLY -> currentDate.minusWeeks(1);
            case MONTHLY -> currentDate.minusMonths(1);
            case YEARLY -> currentDate.minusYears(1);
        };
    }
}
