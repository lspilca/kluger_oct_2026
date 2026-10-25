package com.example.ss_authorization_exercise.service;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Order " + orderId + " not found");
    }
}
