package com.example.banquanao.controller;

import com.example.banquanao.dto.LoginRequest;
import com.example.banquanao.dto.RegisterRequest;
import com.example.banquanao.dto.UserResponse;
import com.example.banquanao.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        authService.register(req);
        return "Đăng ký thành công";
    }
}