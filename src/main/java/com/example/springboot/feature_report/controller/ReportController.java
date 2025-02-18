package com.example.springboot.feature_report.controller;

import com.example.springboot.feature_report.enums.ReportType;
import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling report-related API requests. Provides an endpoint to generate financial
 * reports based on the specified report type.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Constructor for ReportController. Injects the ReportService to handle report generation
     * logic.
     *
     * @param reportService The service responsible for generating reports.
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Endpoint to generate and retrieve a financial report.
     *
     * @param reportType The type of report to generate. Passed as a request parameter.
     * @return The generated report based on the specified type.
     */
    @GetMapping
    public Report getReport(@RequestParam ReportType reportType) {
        // Call the report service to generate the report
        return reportService.generateReport(reportType);
    }
}
