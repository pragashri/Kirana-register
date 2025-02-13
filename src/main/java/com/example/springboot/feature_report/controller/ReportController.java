package com.example.springboot.feature_report.controller;

import com.example.springboot.feature_report.models.Report;
import com.example.springboot.feature_report.services.ReportCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportCacheService reportCacheService;

    @Autowired
    public ReportController(ReportCacheService reportCacheService) {
        this.reportCacheService = reportCacheService;
    }

    @GetMapping
    public Report getReport(@RequestParam String type) {
        return reportCacheService.getReport(type);
    }
}
