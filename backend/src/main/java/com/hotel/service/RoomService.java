package com.hotel.service;

import com.hotel.entity.Room;
import com.hotel.mapper.RoomMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {
    private final RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    public List<Room> getAllRooms() {
        return roomMapper.findAll();
    }

    public Room getRoomById(Long id) {
        return roomMapper.findById(id);
    }

    public List<Room> getAvailableRooms() {
        return roomMapper.findAvailable();
    }

    public List<Room> getRoomsByFloor(Integer floor) {
        return roomMapper.findByFloor(floor);
    }

    public List<Room> getRoomsByRoomType(String roomType) {
        return roomMapper.findByRoomType(roomType);
    }

    public List<Room> getRoomsByFloorAndType(Integer floor, String roomType) {
        return roomMapper.findByFloorAndType(floor, roomType);
    }

    public int addRoom(Room room) {
        return roomMapper.insert(room);
    }

    public int updateRoom(Room room) {
        return roomMapper.update(room);
    }

    public int updateRoomStatus(Long id, String status) {
        return roomMapper.updateStatus(id, status);
    }

    public int deleteRoom(Long id) {
        return roomMapper.delete(id);
    }
}