package com.hotel.mapper;

import com.hotel.entity.HousekeepingTask;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface HousekeepingTaskMapper {
    List<HousekeepingTask> findAll();
    HousekeepingTask findById(Long id);
    List<HousekeepingTask> findByRoomId(Long roomId);
    List<HousekeepingTask> findByStatus(String status);
    int insert(HousekeepingTask task);
    int update(HousekeepingTask task);
    int updateStatus(Long id, String status);
    int delete(Long id);
}