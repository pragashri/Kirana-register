package com.example.springboot.feature_report.services;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_transactions.services.TransactionService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReportServiceImpl implements ReportService {

    private final TransactionService transactionService;

    public ReportServiceImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Report generateReport(ReportType reportType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        switch (reportType) {
            case WEEKLY:
                startDate = now.minusWeeks(1);
                break;
            case MONTHLY:
                startDate = now.minusMonths(1);
                break;
            case YEARLY:
                startDate = now.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }

        return transactionService.generateReport(startDate, now);
    }
}
