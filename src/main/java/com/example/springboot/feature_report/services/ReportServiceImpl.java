package com.example.springboot.feature_report.services;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportService;
import com.example.springboot.feature_report.utils.ReportUtils;
import com.example.springboot.feature_transactions.dao.TransactionDao;
import com.example.springboot.feature_transactions.entities.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final TransactionDao transactionDao;
    private final CacheService cacheService;

    public ReportServiceImpl(TransactionDao transactionDao, CacheService cacheService) {
        this.transactionDao = transactionDao;
        this.cacheService = cacheService;
    }

    @Override
    public Report generateReport(ReportType reportType, LocalDateTime startDate, LocalDateTime endDate) {
        String cacheKey = "report_" + reportType.name();

        Report cachedReport = cacheService.get(cacheKey, Report.class);
        if (cachedReport != null) {
            System.out.println("Fetching report from cache...");
            return cachedReport;
        }
        else{
            System.out.println("Fetching report from DB...");
            List<Transaction> transactions = transactionDao.findTransactionsBetween(startDate, endDate);
            Report report = ReportUtils.finalizeReport(transactions);

            System.out.println("Setting " + report + " cache...");
            cacheService.put(cacheKey, report, 3600);
            return report;
        }




    }

}
