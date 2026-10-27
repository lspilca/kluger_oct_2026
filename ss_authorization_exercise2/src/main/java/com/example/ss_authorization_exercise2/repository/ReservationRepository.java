package com.example.ss_authorization_exercise2.repository;

import com.example.ss_authorization_exercise2.entity.Reservation;
import com.example.ss_authorization_exercise2.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByIdAndOwnerUsername(Long id, String ownerUsername);

    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.room.id = :roomId
              AND r.status IN :statuses
              AND r.startTime < :endTime
              AND r.endTime > :startTime
            """)
    boolean existsOverlapping(@Param("roomId") Long roomId,
                              @Param("statuses") Collection<ReservationStatus> statuses,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);
}
