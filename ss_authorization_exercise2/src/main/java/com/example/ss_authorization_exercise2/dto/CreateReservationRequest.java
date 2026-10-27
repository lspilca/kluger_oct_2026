package com.example.ss_authorization_exercise2.dto;

import java.time.LocalDateTime;

public record CreateReservationRequest(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
}
