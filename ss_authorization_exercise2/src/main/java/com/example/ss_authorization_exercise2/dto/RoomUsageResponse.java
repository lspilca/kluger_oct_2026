package com.example.ss_authorization_exercise2.dto;

import java.util.Map;


public record RoomUsageResponse(Long roomId,
                                String roomName,
                                int capacity,
                                long totalReservations,
                                Map<String, Long> reservationsByStatus,
                                double bookedHours) {
}
