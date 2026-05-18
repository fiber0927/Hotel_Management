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

    @GetMapping("/floor/{floor}")
    public List<Room> getRoomsByFloor(@PathVariable Integer floor) {
        return roomService.getRoomsByFloor(floor);
    }

    @GetMapping("/type/{roomType}")
    public List<Room> getRoomsByRoomType(@PathVariable String roomType) {
        return roomService.getRoomsByRoomType(roomType);
    }

    @GetMapping("/floor/{floor}/type/{roomType}")
    public List<Room> getRoomsByFloorAndType(@PathVariable Integer floor, @PathVariable String roomType) {
        return roomService.getRoomsByFloorAndType(floor, roomType);
    }

    @GetMapping("/stats")
    public Map<String, Object> getRoomStats() {
        List<Room> allRooms = roomService.getAllRooms();
        Map<String, Object> stats = new java.util.HashMap<>();

        Map<String, Map<String, Integer>> byFloor = new java.util.LinkedHashMap<>();
        for (int floor = 1; floor <= 4; floor++) {
            Map<String, Integer> typeCount = new java.util.HashMap<>();
            typeCount.put("standard", 0);
            typeCount.put("suite", 0);
            typeCount.put("total", 0);
            byFloor.put(String.valueOf(floor), typeCount);
        }

        Map<String, Integer> byType = new java.util.HashMap<>();
        byType.put("standard", 0);
        byType.put("suite", 0);

        for (Room room : allRooms) {
            String floor = String.valueOf(room.getFloor());
            String type = room.getRoomType();
            byFloor.get(floor).put(type, byFloor.get(floor).get(type) + 1);
            byFloor.get(floor).put("total", byFloor.get(floor).get("total") + 1);
            byType.put(type, byType.get(type) + 1);
        }

        stats.put("byFloor", byFloor);
        stats.put("byType", byType);
        stats.put("total", allRooms.size());
        return stats;
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