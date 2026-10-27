package com.example.ss_authorization_exercise2.controller;

import com.example.ss_authorization_exercise2.dto.CreateReservationRequest;
import com.example.ss_authorization_exercise2.dto.ReservationResponse;
import com.example.ss_authorization_exercise2.dto.StatusChangeRequest;
import com.example.ss_authorization_exercise2.entity.Reservation;
import com.example.ss_authorization_exercise2.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    static final String ANONYMOUS_OWNER = "anonymousUser";

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody CreateReservationRequest request,
                                                                 Authentication authentication) {
        String owner = authentication != null ? authentication.getName() : ANONYMOUS_OWNER;
        Reservation reservation = reservationService.createReservation(
                request.roomId(), request.startTime(), request.endTime(), owner);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}").buildAndExpand(reservation.getId()).toUri())
                .body(ReservationResponse.from(reservation));
    }

    @GetMapping("/{id}")
    public ReservationResponse getReservation(@PathVariable Long id) {
        return ReservationResponse.from(reservationService.getReservation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelAnyReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable Long id) {
        reservationService.approveReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestBody StatusChangeRequest request) {
        reservationService.changeReservationStatus(id, request.status());
        return ResponseEntity.noContent().build();
    }
}
