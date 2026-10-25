package com.example.ss_authorization_exercise.web;

import com.example.ss_authorization_exercise.model.OrderReport;
import com.example.ss_authorization_exercise.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminReportController {

    private final OrderService orderService;

    public AdminReportController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/reports")
    public OrderReport report() {
        return orderService.generateReport();
    }
}
