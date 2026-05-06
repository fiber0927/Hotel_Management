package com.hotel.controller;

import com.hotel.entity.Booking;
import com.hotel.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/guest/{guestId}")
    public List<Booking> getBookingsByGuestId(@PathVariable Long guestId) {
        return bookingService.getBookingsByGuestId(guestId);
    }

    @GetMapping("/status/{status}")
    public List<Booking> getBookingsByStatus(@PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }

    @PostMapping
    public ResponseEntity<?> addBooking(@RequestBody Booking booking) {
        try {
            // Validate required fields
            if (booking.getGuestId() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "客人ID不能为空"));
            }
            if (booking.getRoomId() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "房间ID不能为空"));
            }
            if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "入住日期和退房日期不能为空"));
            }
            if (booking.getCheckOutDate().isBefore(booking.getCheckInDate()) || booking.getCheckOutDate().equals(booking.getCheckInDate())) {
                return ResponseEntity.badRequest().body(Map.of("message", "退房日期必须晚于入住日期"));
            }
            if (booking.getStatus() == null) {
                booking.setStatus("pending");
            }
            int result = bookingService.addBooking(booking);
            return ResponseEntity.ok(Map.of("id", result, "message", "预订成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "预订失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public int updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        booking.setId(id);
        return bookingService.updateBooking(booking);
    }

    @PatchMapping("/{id}/cancel")
    public int cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @PatchMapping("/{id}/complete")
    public int completeBooking(@PathVariable Long id) {
        return bookingService.completeBooking(id);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        try {
            int result = bookingService.confirmBooking(id);
            if (result > 0) {
                return ResponseEntity.ok(Map.of("message", "预订已确认", "id", id));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "预订不存在或确认失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "确认失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            int result = bookingService.deleteBooking(id);
            if (result > 0) {
                return ResponseEntity.ok(Map.of("message", "预订已删除"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "预订不存在或删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "删除失败: " + e.getMessage()));
        }
    }
}