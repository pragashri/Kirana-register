package com.example.springboot.feature_report.services;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;

public interface ReportService {
    /**
     * Generates a financial report for the given report type.
     *
     * @param reportType
     * @return
     */
    Report generateReport(ReportType reportType);
}
