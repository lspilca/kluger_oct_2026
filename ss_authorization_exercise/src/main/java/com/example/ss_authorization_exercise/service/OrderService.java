package com.example.ss_authorization_exercise.service;

import com.example.ss_authorization_exercise.model.Order;
import com.example.ss_authorization_exercise.model.OrderReport;
import com.example.ss_authorization_exercise.model.OrderStatus;
import com.example.ss_authorization_exercise.model.Product;
import com.example.ss_authorization_exercise.repository.OrderRepository;
import com.example.ss_authorization_exercise.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(String ownerUsername, Long productId, int quantity) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new InvalidOrderRequestException("Unknown product id: " + productId);
        }
        if (quantity < 1) {
            throw new InvalidOrderRequestException("Quantity must be at least 1");
        }
        return orderRepository.create(ownerUsername, productId, quantity);
    }

    public void cancelAnyOrder(Long orderId) {
        orderRepository.update(orderId, order -> order.withStatus(OrderStatus.CANCELLED))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public void refundOrder(Long orderId) {
        orderRepository.update(orderId, order -> order.withRefunded(true))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public void updateOrderStatus(Long orderId, String status) {
        OrderStatus newStatus = parseStatus(status);
        orderRepository.update(orderId, order -> order.withStatus(newStatus))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public OrderReport generateReport() {
        List<Order> orders = orderRepository.findAll();

        Map<OrderStatus, Long> byStatus = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            byStatus.put(status, 0L);
        }
        long refunded = 0;
        BigDecimal revenue = BigDecimal.ZERO;

        for (Order order : orders) {
            byStatus.merge(order.status(), 1L, Long::sum);
            if (order.refunded()) {
                refunded++;
            } else if (order.status() != OrderStatus.CANCELLED) {
                revenue = revenue.add(lineTotal(order));
            }
        }
        return new OrderReport(orders.size(), byStatus, refunded, revenue);
    }

    private BigDecimal lineTotal(Order order) {
        return productRepository.findById(order.productId())
                .map(Product::price)
                .map(price -> price.multiply(BigDecimal.valueOf(order.quantity())))
                .orElse(BigDecimal.ZERO);
    }

     private static OrderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new InvalidOrderRequestException("Status is required");
        }
        try {
            return OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderRequestException("Unknown order status: " + status);
        }
    }
}
