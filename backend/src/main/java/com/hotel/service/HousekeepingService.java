package com.hotel.service;

import com.hotel.entity.HousekeepingTask;
import com.hotel.mapper.HousekeepingTaskMapper;
import com.hotel.mapper.RoomMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HousekeepingService {
    private final HousekeepingTaskMapper taskMapper;
    private final RoomMapper roomMapper;

    public HousekeepingService(HousekeepingTaskMapper taskMapper, RoomMapper roomMapper) {
        this.taskMapper = taskMapper;
        this.roomMapper = roomMapper;
    }

    public List<HousekeepingTask> getAllTasks() {
        return taskMapper.findAll();
    }

    public HousekeepingTask getTaskById(Long id) {
        return taskMapper.findById(id);
    }

    public List<HousekeepingTask> getTasksByRoomId(Long roomId) {
        return taskMapper.findByRoomId(roomId);
    }

    public List<HousekeepingTask> getTasksByStatus(String status) {
        return taskMapper.findByStatus(status);
    }

    public int addTask(HousekeepingTask task) {
        return taskMapper.insert(task);
    }

    public int updateTask(HousekeepingTask task) {
        return taskMapper.update(task);
    }

    // 完成任务 → 打扫完成，但房间不自动恢复可用，需等退房确认
    @Transactional
    public int completeTask(Long id) {
        HousekeepingTask task = taskMapper.findById(id);
        if (task != null) {
            task.setStatus("completed");
            task.setCompletedAt(LocalDateTime.now());
            return taskMapper.update(task);
            // 注意：不再自动设置房间为 available，需等管理员确认退房
        }
        return 0;
    }

    public int deleteTask(Long id) {
        return taskMapper.delete(id);
    }
}
