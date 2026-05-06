package com.hotel.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Bill {
    private Long id;
    private Long checkInId;
    private BigDecimal roomCharge;
    private BigDecimal otherCharges;
    private BigDecimal totalAmount;
    private Integer isPaid;
    private String paymentMethod;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime updatedAt;
}