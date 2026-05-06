package com.hotel.controller;

import com.hotel.entity.Admin;
import com.hotel.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", admin.getId());
            result.put("username", admin.getUsername());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }
}