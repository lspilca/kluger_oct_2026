package com.example.ss_authorization_exercise2.security;

import com.example.ss_authorization_exercise2.repository.ReservationRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
public class ReservationSecurity {

    private final ReservationRepository reservationRepository;

    public ReservationSecurity(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean isOwner(Long reservationId, Authentication authentication) {
        if (reservationId == null
                || authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return reservationRepository.existsByIdAndOwnerUsername(reservationId, authentication.getName());
    }
}
