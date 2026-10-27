package com.example.ss_authorization_exercise2.dto;

import com.example.ss_authorization_exercise2.entity.Room;

public record RoomResponse(Long id, String name, int capacity, String location) {

    public static RoomResponse from(Room room) {
        return new RoomResponse(room.getId(), room.getName(), room.getCapacity(), room.getLocation());
    }
}
