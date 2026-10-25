package com.example.ss_authorization_exercise.service;

/**
 * Raised for business-level validation failures (unknown product, unknown status, ...).
 */
public class InvalidOrderRequestException extends RuntimeException {

    public InvalidOrderRequestException(String message) {
        super(message);
    }
}
