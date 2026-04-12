package com.example.banquanao.controller;

import com.example.banquanao.dto.UserDTO;
import com.example.banquanao.model.User;
import com.example.banquanao.repository.CategoryRepository;
import com.example.banquanao.repository.UserRepository;
import com.example.banquanao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserRepository repository;
    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return service.findAll();
    }
    // Lấy tất cả users (Admin)
    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = repository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
