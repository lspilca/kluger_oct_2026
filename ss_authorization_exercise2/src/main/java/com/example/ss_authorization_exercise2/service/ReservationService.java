package com.example.ss_authorization_exercise2.service;

import com.example.ss_authorization_exercise2.entity.Reservation;
import com.example.ss_authorization_exercise2.entity.ReservationStatus;
import com.example.ss_authorization_exercise2.entity.Room;
import com.example.ss_authorization_exercise2.repository.ReservationRepository;
import com.example.ss_authorization_exercise2.repository.RoomRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Service
public class ReservationService {

    private static final Set<ReservationStatus> BLOCKING_STATUSES =
            EnumSet.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Reservation createReservation(Long roomId, LocalDateTime startTime, LocalDateTime endTime,
                                         String ownerUsername) {
        if (roomId == null || startTime == null || endTime == null) {
            throw new InvalidRequestException("roomId, startTime and endTime are required");
        }
        if (!startTime.isBefore(endTime)) {
            throw new InvalidRequestException("startTime must be before endTime");
        }
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room " + roomId + " not found"));
        if (reservationRepository.existsOverlapping(roomId, BLOCKING_STATUSES, startTime, endTime)) {
            throw new ReservationConflictException(
                    "Room " + roomId + " is already reserved in the requested time slot");
        }
        return reservationRepository.save(
                new Reservation(room, ownerUsername, startTime, endTime, ReservationStatus.PENDING));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('COORDINATOR', 'ADMIN')")
    public void cancelAnyReservation(Long reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation.getStatus() == ReservationStatus.CANCELLED
                || reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new ReservationConflictException(
                    "Reservation " + reservationId + " is " + reservation.getStatus() + " and cannot be cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROOM_APPROVE')")
    public void approveReservation(Long reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new ReservationConflictException("Only PENDING reservations can be approved; reservation "
                    + reservationId + " is " + reservation.getStatus());
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@reservationSecurity.isOwner(#reservationId) or hasRole('ADMIN')")
    public Reservation getReservation(Long reservationId) {
        return findReservation(reservationId);
    }

    @Transactional
    @PreAuthorize("@reservationSecurity.changeReservationStatusAllowed(#status)")
    public void changeReservationStatus(Long reservationId, String status) {
        ReservationStatus newStatus = parseStatus(status);
        Reservation reservation = findReservation(reservationId);
        reservation.setStatus(newStatus);
    }

    private Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation " + reservationId + " not found"));
    }

    private static ReservationStatus parseStatus(String status) {
        if (status == null) {
            throw new InvalidRequestException("status is required");
        }
        try {
            return ReservationStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Unknown reservation status: " + status);
        }
    }
}
