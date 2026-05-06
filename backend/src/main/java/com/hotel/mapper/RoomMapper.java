package com.hotel.mapper;

import com.hotel.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoomMapper {
    List<Room> findAll();
    Room findById(Long id);
    List<Room> findAvailable();
    int insert(Room room);
    int update(Room room);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    int delete(Long id);
}