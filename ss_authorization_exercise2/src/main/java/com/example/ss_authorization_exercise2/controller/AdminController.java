package com.example.ss_authorization_exercise2.controller;

import com.example.ss_authorization_exercise2.dto.RoomUsageResponse;
import com.example.ss_authorization_exercise2.service.UsageReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UsageReportService usageReportService;

    public AdminController(UsageReportService usageReportService) {
        this.usageReportService = usageReportService;
    }

    @GetMapping("/usage")
    public List<RoomUsageResponse> usage() {
        return usageReportService.getUsageReport();
    }
}
