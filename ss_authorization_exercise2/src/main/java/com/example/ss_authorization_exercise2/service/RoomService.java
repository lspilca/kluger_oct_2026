package com.example.ss_authorization_exercise2.service;

import com.example.ss_authorization_exercise2.entity.Room;
import com.example.ss_authorization_exercise2.repository.RoomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<Room> listRooms() {
        return roomRepository.findAll(Sort.by("id"));
    }
}
