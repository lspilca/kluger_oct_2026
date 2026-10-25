package com.example.ss_authorization_exercise.model;


public record Order(
        Long id,
        String ownerUsername,
        Long productId,
        int quantity,
        OrderStatus status,
        boolean refunded) {

    public Order withStatus(OrderStatus newStatus) {
        return new Order(id, ownerUsername, productId, quantity, newStatus, refunded);
    }

    public Order withRefunded(boolean newRefunded) {
        return new Order(id, ownerUsername, productId, quantity, status, newRefunded);
    }
}
