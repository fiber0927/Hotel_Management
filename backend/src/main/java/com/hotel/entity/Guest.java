package com.hotel.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Guest {
    private Long id;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}