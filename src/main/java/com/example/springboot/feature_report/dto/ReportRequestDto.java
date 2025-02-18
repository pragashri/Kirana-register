package com.example.springboot.feature_report.dto;

import com.example.springboot.feature_report.enums.ReportType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportRequestDto {

    private ReportType reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
