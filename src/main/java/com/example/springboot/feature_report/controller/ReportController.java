package com.example.springboot.feature_report.controller;

import com.example.springboot.feature_report.dto.ReportRequestDto;
import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    // Constructor injection of ReportService only
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Pass ReportType as a request parameter, no need for a class-level field
    @GetMapping
    public Report getReport(@RequestParam ReportType reportType) {
        // Call the report service to generate the report
        return reportService.generateReport(reportType);
    }
}
