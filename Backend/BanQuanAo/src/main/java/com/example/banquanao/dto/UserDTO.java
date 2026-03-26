package com.example.banquanao.dto;


public class UserDTO {

    private Integer maUser;
    private String name;
    private String email;
    private Integer soDienThoai;

    public UserDTO() {
    }

    public UserDTO(Integer maUser, String name, String email, Integer soDienThoai) {
        this.maUser = maUser;
        this.name = name;
        this.email = email;
        this.soDienThoai = soDienThoai;
    }

    public Integer getMaUser() {
        return maUser;
    }

    public void setMaUser(Integer maUser) {
        this.maUser = maUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
