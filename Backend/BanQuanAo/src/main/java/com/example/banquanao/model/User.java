package com.example.banquanao.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maUser;

    private String ten;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private Integer soDienThoai;

    private String matKhau;

    private String vaiTro;

    private Boolean trangThai = true;

    public User() {
    }

    public User(Integer maUser, String ten, String email, Integer soDienThoai, String matKhau, String vaiTro, Boolean trangThai) {
        this.maUser = maUser;
        this.ten = ten;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
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

    public Integer getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(Integer soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
}