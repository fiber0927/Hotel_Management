package com.hotel.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CheckInRecord {
    private Long id;
    private Long bookingId;
    private Long roomId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}