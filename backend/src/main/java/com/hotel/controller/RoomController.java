package com.hotel.controller;

import com.hotel.entity.Room;
import com.hotel.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @PostMapping
    public int addRoom(@RequestBody Room room) {
        if (room.getStatus() == null) {
            room.setStatus("available");
        }
        return roomService.addRoom(room);
    }

    @PutMapping("/{id}")
    public int updateRoom(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        return roomService.updateRoom(room);
    }

    @PatchMapping("/{id}/status")
    public int updateRoomStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        return roomService.updateRoomStatus(id, statusData.get("status"));
    }

    @DeleteMapping("/{id}")
    public int deleteRoom(@PathVariable Long id) {
        return roomService.deleteRoom(id);
    }
}