package com.example.ss_authorization_exercise2.repository;

import com.example.ss_authorization_exercise2.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
