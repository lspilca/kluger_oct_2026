package com.example.ss_authorization_exercise2.service;

import com.example.ss_authorization_exercise2.dto.RoomUsageResponse;
import com.example.ss_authorization_exercise2.entity.Reservation;
import com.example.ss_authorization_exercise2.entity.ReservationStatus;
import com.example.ss_authorization_exercise2.entity.Room;
import com.example.ss_authorization_exercise2.repository.ReservationRepository;
import com.example.ss_authorization_exercise2.repository.RoomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsageReportService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public UsageReportService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomUsageResponse> getUsageReport() {
        Map<Long, List<Reservation>> reservationsByRoom = reservationRepository.findAll().stream()
                .collect(Collectors.groupingBy(r -> r.getRoom().getId()));

        return roomRepository.findAll(Sort.by("id")).stream()
                .map(room -> toUsage(room, reservationsByRoom.getOrDefault(room.getId(), List.of())))
                .toList();
    }

    private static RoomUsageResponse toUsage(Room room, List<Reservation> reservations) {
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (ReservationStatus status : ReservationStatus.values()) {
            byStatus.put(status.name(), reservations.stream().filter(r -> r.getStatus() == status).count());
        }
        long bookedMinutes = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED
                        || r.getStatus() == ReservationStatus.COMPLETED)
                .mapToLong(r -> Duration.between(r.getStartTime(), r.getEndTime()).toMinutes())
                .sum();
        return new RoomUsageResponse(room.getId(), room.getName(), room.getCapacity(),
                reservations.size(), byStatus, bookedMinutes / 60.0);
    }
}
