package com.hotel.controller;

import com.hotel.entity.CheckInRecord;
import com.hotel.service.CheckInService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping
    public List<CheckInRecord> getAllRecords() {
        return checkInService.getAllRecords();
    }

    @GetMapping("/{id}")
    public CheckInRecord getRecordById(@PathVariable Long id) {
        return checkInService.getRecordById(id);
    }

    @GetMapping("/status/{status}")
    public List<CheckInRecord> getRecordsByStatus(@PathVariable String status) {
        return checkInService.getRecordsByStatus(status);
    }

    @GetMapping("/booking/{bookingId}")
    public CheckInRecord getRecordByBookingId(@PathVariable Long bookingId) {
        return checkInService.getRecordByBookingId(bookingId);
    }

    @PostMapping
    public int checkIn(@RequestBody CheckInRecord record) {
        return checkInService.checkIn(record);
    }

    @PostMapping("/{id}/checkout")
    public int checkOut(@PathVariable Long id) {
        return checkInService.checkOut(id);
    }

    // 管理员确认退房 → 房间恢复可用
    @PostMapping("/{id}/confirm-checkout")
    public int confirmCheckout(@PathVariable Long id) {
        return checkInService.confirmCheckout(id);
    }
}
