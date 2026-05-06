package com.hotel.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Customer {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
}