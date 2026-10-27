package com.example.ss_authorization_exercise2.dto;

import com.example.ss_authorization_exercise2.entity.Reservation;

import java.time.LocalDateTime;

public record ReservationResponse(Long id,
                                  Long roomId,
                                  String roomName,
                                  String ownerUsername,
                                  LocalDateTime startTime,
                                  LocalDateTime endTime,
                                  String status) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoom().getId(),
                reservation.getRoom().getName(),
                reservation.getOwnerUsername(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus().name());
    }
}
