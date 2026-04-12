package com.example.banquanao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


public class UserResponse {
    private Integer maUser;
    private String ten;
    private String email;
    private String vaiTro;

    public UserResponse() {
    }

    public UserResponse(Integer maUser, String ten, String email, String vaiTro) {
        this.maUser = maUser;
        this.ten = ten;
        this.email = email;
        this.vaiTro = vaiTro;
    }

    public Integer getMaUser() {
        return maUser;
    }

    public void setMaUser(Integer maUser) {
        this.maUser = maUser;
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

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}