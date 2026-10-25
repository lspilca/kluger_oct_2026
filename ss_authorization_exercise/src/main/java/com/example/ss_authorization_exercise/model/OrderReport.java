package com.example.ss_authorization_exercise.model;

import java.math.BigDecimal;
import java.util.Map;

public record OrderReport(
        int totalOrders,
        Map<OrderStatus, Long> ordersByStatus,
        long refundedOrders,
        BigDecimal totalRevenue) {
}
