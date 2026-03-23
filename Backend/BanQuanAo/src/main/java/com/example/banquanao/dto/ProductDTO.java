package com.example.banquanao.dto;

public class ProductDTO {

    private Long id;
    private String name;
    private Double price;
    private String category;
    private String size;
    private String color;
    private Integer stock;
    private String image;
    private String status;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Double price, String category, String size, String color, Integer stock, String image, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.size = size;
        this.color = color;
        this.stock = stock;
        this.image = image;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}