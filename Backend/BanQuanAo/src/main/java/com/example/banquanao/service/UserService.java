package com.example.banquanao.service;

import com.example.banquanao.dto.UserDTO;
import com.example.banquanao.model.User;
import com.example.banquanao.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;


    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    private UserDTO toDTO(User u) {
        return new UserDTO(
                u.getMaUser(),
                u.getTen(),
                u.getEmail(),
                u.getSoDienThoai()
        );
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
}
