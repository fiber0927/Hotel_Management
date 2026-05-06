package com.hotel.controller;

import com.hotel.entity.Customer;
import com.hotel.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Customer customer = customerService.login(username, password);
        if (customer != null) {
            return ResponseEntity.ok(Map.of(
                "id", customer.getId(),
                "username", customer.getUsername(),
                "name", customer.getName() != null ? customer.getName() : "",
                "phone", customer.getPhone() != null ? customer.getPhone() : ""
            ));
        }
        return ResponseEntity.status(401).body(Map.of("message", "用户名或密码错误"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> regData) {
        String username = regData.get("username");
        String password = regData.get("password");
        String phone = regData.get("phone");

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户名不能为空"));
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "密码不能为空"));
        }
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "手机号格式不正确"));
        }

        // 检查用户名是否已存在
        Customer existing = customerService.findByUsername(username);
        if (existing != null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户名已被注册"));
        }

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setPhone(phone);
        customer.setName(username);

        int result = customerService.register(customer);
        if (result > 0) {
            return ResponseEntity.ok(Map.of("message", "注册成功，请登录"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "注册失败"));
    }
}
