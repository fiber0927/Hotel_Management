package com.hotel.controller;

import com.hotel.entity.HousekeepingTask;
import com.hotel.service.HousekeepingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/housekeeping")
public class HousekeepingController {
    private final HousekeepingService housekeepingService;

    public HousekeepingController(HousekeepingService housekeepingService) {
        this.housekeepingService = housekeepingService;
    }

    @GetMapping
    public List<HousekeepingTask> getAllTasks() {
        return housekeepingService.getAllTasks();
    }

    @GetMapping("/{id}")
    public HousekeepingTask getTaskById(@PathVariable Long id) {
        return housekeepingService.getTaskById(id);
    }

    @GetMapping("/room/{roomId}")
    public List<HousekeepingTask> getTasksByRoomId(@PathVariable Long roomId) {
        return housekeepingService.getTasksByRoomId(roomId);
    }

    @GetMapping("/status/{status}")
    public List<HousekeepingTask> getTasksByStatus(@PathVariable String status) {
        return housekeepingService.getTasksByStatus(status);
    }

    @PostMapping
    public int addTask(@RequestBody HousekeepingTask task) {
        if (task.getStatus() == null) {
            task.setStatus("pending");
        }
        if (task.getPriority() == null) {
            task.setPriority("normal");
        }
        return housekeepingService.addTask(task);
    }

    @PutMapping("/{id}")
    public int updateTask(@PathVariable Long id, @RequestBody HousekeepingTask task) {
        task.setId(id);
        return housekeepingService.updateTask(task);
    }

    @PatchMapping("/{id}/complete")
    public int completeTask(@PathVariable Long id) {
        return housekeepingService.completeTask(id);
    }

    @DeleteMapping("/{id}")
    public int deleteTask(@PathVariable Long id) {
        return housekeepingService.deleteTask(id);
    }
}