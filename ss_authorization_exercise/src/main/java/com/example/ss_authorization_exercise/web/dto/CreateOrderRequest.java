package com.example.ss_authorization_exercise.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Body of POST /api/orders. The owner is never taken from the request - it comes from the
 * authenticated principal.
 */
public record CreateOrderRequest(
        @NotNull Long productId,
        @NotNull @Min(1) @Max(100) Integer quantity) {
}
