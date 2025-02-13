package com.example.springboot.feature_report.services;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    Report generateReport(ReportType reportType);
}
