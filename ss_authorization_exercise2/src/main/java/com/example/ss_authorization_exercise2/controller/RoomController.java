package com.example.ss_authorization_exercise2.controller;

import com.example.ss_authorization_exercise2.dto.RoomResponse;
import com.example.ss_authorization_exercise2.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomResponse> listRooms() {
        return roomService.listRooms().stream().map(RoomResponse::from).toList();
    }
}
