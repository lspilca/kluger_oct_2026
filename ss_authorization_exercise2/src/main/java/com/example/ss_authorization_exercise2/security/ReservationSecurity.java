package com.example.ss_authorization_exercise2.security;

import com.example.ss_authorization_exercise2.repository.ReservationRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ReservationSecurity {

    private final ReservationRepository reservationRepository;

    public ReservationSecurity(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean isOwner(Long reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (reservationId == null
                || authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return reservationRepository.existsByIdAndOwnerUsername(reservationId, authentication.getName());
    }

    public boolean changeReservationStatusAllowed(String status) {
        List<String> roles =
                SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        if ("CONFIRMED".equals(status) || "COMPLETED".equals(status) || "REJECTED".equals(status)) {
            if (roles.contains("ROLE_ADMIN")) return true;
            if (roles.contains("ROLE_COORDINATOR")) {
                if ("CONFIRMED".equals(status) || "COMPLETED".equals(status)) {
                    return true;
                }
            }
        }

        return false;
    }
}
