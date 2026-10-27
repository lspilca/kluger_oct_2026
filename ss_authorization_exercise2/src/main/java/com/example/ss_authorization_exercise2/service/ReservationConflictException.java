package com.example.ss_authorization_exercise2.service;

/** Thrown when an operation conflicts with the current reservation state. Mapped to HTTP 409. */
public class ReservationConflictException extends RuntimeException {

    public ReservationConflictException(String message) {
        super(message);
    }
}
