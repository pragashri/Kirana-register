package com.example.springboot.feature_report.services;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_transactions.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportCacheService {

    private final TransactionService transactionService;
    private final CacheService cacheService;

    @Autowired
    public ReportCacheService(TransactionService transactionService, CacheService cacheService) {
        this.transactionService = transactionService;
        this.cacheService = cacheService;
    }

    public Report getReport(String reportType) {
        String cacheKey = "report_" + reportType;

        Report cachedReport = cacheService.get(cacheKey, Report.class);

        if (cachedReport != null) {
            System.out.println("✅ Fetching from CACHE: " + reportType);
            return cachedReport;
        }

        // Fetch from DB if cache is empty
        System.out.println("❌ Cache MISS, Fetching from DB: " + reportType);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        switch (reportType.toLowerCase()) {
            case "weekly":
                startDate = now.minusWeeks(1);
                break;
            case "monthly":
                startDate = now.minusMonths(1);
                break;
            case "yearly":
                startDate = now.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }

        Report report = transactionService.generateReport(startDate, now);

        // Store in cache
        cacheService.put(cacheKey, report, 3600);
        return report;
    }
}
