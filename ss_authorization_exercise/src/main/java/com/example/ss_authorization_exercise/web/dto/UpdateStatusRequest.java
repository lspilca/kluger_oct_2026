package com.example.ss_authorization_exercise.web.dto;

import jakarta.validation.constraints.NotBlank;

/** Body of PATCH /api/orders/{id}/status, e.g. {"status":"PROCESSING"}. */
public record UpdateStatusRequest(@NotBlank String status) {
}
