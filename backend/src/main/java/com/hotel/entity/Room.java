package com.hotel.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Room {
    private Long id;
    private String roomNumber;
    private String roomType;
    private Integer floor;
    private String status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}