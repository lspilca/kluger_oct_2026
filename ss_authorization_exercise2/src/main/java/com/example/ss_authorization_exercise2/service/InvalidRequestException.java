package com.example.ss_authorization_exercise2.service;

/** Thrown when request data is invalid (missing fields, bad time range, unknown status). Mapped to HTTP 400. */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
