package com.example.ss_authorization_exercise2.config;

import com.example.ss_authorization_exercise2.entity.Reservation;
import com.example.ss_authorization_exercise2.entity.ReservationStatus;
import com.example.ss_authorization_exercise2.entity.Room;
import com.example.ss_authorization_exercise2.repository.ReservationRepository;
import com.example.ss_authorization_exercise2.repository.RoomRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.ss_authorization_exercise2.entity.ReservationStatus.CANCELLED;
import static com.example.ss_authorization_exercise2.entity.ReservationStatus.COMPLETED;
import static com.example.ss_authorization_exercise2.entity.ReservationStatus.CONFIRMED;
import static com.example.ss_authorization_exercise2.entity.ReservationStatus.PENDING;
import static com.example.ss_authorization_exercise2.entity.ReservationStatus.REJECTED;

@Component
public class DataInitializer implements ApplicationRunner {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public DataInitializer(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (roomRepository.count() > 0) {
            return;
        }

        Room ada = roomRepository.save(new Room("Ada Lovelace", 12, "Building A, Floor 1"));
        Room turing = roomRepository.save(new Room("Alan Turing", 20, "Building A, Floor 2"));
        Room hopper = roomRepository.save(new Room("Grace Hopper", 8, "Building B, Floor 1"));
        Room liskov = roomRepository.save(new Room("Barbara Liskov", 30, "Building B, Auditorium"));
        Room hamilton = roomRepository.save(new Room("Margaret Hamilton", 6, "Building C, Floor 3"));

        save(ada, "alice", "2026-11-02T09:00", "2026-11-02T12:00", PENDING);       // 1
        save(turing, "alice", "2026-11-05T10:00", "2026-11-05T12:00", CONFIRMED);  // 2
        save(ada, "bob", "2026-11-03T09:00", "2026-11-03T17:00", CONFIRMED);       // 3
        save(hopper, "bob", "2026-11-06T09:00", "2026-11-06T11:00", PENDING);      // 4
        save(liskov, "emma", "2026-10-20T09:00", "2026-10-20T17:00", COMPLETED);   // 5
        save(hamilton, "carl", "2026-11-09T14:00", "2026-11-09T16:00", CONFIRMED); // 6
        save(hopper, "alice", "2026-11-06T09:00", "2026-11-06T11:00", REJECTED);   // 7
        save(turing, "bob", "2026-11-05T10:00", "2026-11-05T12:00", CANCELLED);    // 8
        save(ada, "cora", "2026-11-04T13:00", "2026-11-04T17:00", PENDING);        // 9
        save(hamilton, "emma", "2026-11-10T09:00", "2026-11-10T10:00", PENDING);   // 10
    }

    private void save(Room room, String owner, String start, String end, ReservationStatus status) {
        reservationRepository.save(
                new Reservation(room, owner, LocalDateTime.parse(start), LocalDateTime.parse(end), status));
    }
}
