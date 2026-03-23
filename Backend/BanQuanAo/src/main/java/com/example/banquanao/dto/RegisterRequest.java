package com.example.banquanao.dto;

import lombok.Data;

public class RegisterRequest {
    private String ten;
    private String email;
    private String matKhau;

    public RegisterRequest(String ten, String email, String matKhau) {
        this.ten = ten;
        this.email = email;
        this.matKhau = matKhau;
    }

    public RegisterRequest() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}