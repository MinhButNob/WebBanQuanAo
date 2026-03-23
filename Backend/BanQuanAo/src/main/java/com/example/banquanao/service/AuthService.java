package com.example.banquanao.service;

import com.example.banquanao.dto.LoginRequest;
import com.example.banquanao.dto.RegisterRequest;
import com.example.banquanao.dto.UserResponse;
import com.example.banquanao.model.User;
import com.example.banquanao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        if (!user.getMatKhau().equals(req.getMatKhau())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        return new UserResponse(
                user.getMaUser(),
                user.getTen(),
                user.getEmail(),
                user.getVaiTro()
        );
    }

    public void register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setTen(req.getTen());
        user.setEmail(req.getEmail());
        user.setMatKhau(req.getMatKhau());
        user.setVaiTro("USER");

        userRepository.save(user);
    }
}