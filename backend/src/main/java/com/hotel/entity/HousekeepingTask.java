package com.hotel.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HousekeepingTask {
    private Long id;
    private Long roomId;
    private String taskType;
    private String priority;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}