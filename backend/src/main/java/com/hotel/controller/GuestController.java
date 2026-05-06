package com.hotel.controller;

import com.hotel.entity.Guest;
import com.hotel.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    @GetMapping("/{id}")
    public Guest getGuestById(@PathVariable Long id) {
        return guestService.getGuestById(id);
    }

    @PostMapping
    public ResponseEntity<?> addGuest(@RequestBody Guest guest) {
        try {
            // Validate input
            if (guest.getPhone() == null || guest.getPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "手机号不能为空"));
            }
            if (guest.getName() == null || guest.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "姓名不能为空"));
            }
            // Phone validation: 11 digits
            String phone = guest.getPhone().trim();
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return ResponseEntity.badRequest().body(Map.of("message", "手机号格式不正确"));
            }
            Long id = guestService.addGuest(guest);
            return ResponseEntity.ok(Map.of("id", id, "message", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "创建客户信息失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public int updateGuest(@PathVariable Long id, @RequestBody Guest guest) {
        guest.setId(id);
        return guestService.updateGuest(guest);
    }

    @DeleteMapping("/{id}")
    public int deleteGuest(@PathVariable Long id) {
        return guestService.deleteGuest(id);
    }
}