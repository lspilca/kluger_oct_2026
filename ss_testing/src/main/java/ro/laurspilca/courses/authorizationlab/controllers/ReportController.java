package ro.laurspilca.courses.authorizationlab.controllers;

import ro.laurspilca.courses.authorizationlab.model.ExportResponse;
import ro.laurspilca.courses.authorizationlab.model.SummaryResponse;
import ro.laurspilca.courses.authorizationlab.services.ReportService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/export")
    public ExportResponse export() {
        return new ExportResponse(reportService.exportId());
    }

    @GetMapping("/summary")
    public SummaryResponse summary() {
        return new SummaryResponse(reportService.documentCount());
    }
}
