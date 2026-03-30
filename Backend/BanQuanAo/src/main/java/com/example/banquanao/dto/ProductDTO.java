package com.example.banquanao.dto;



public class ProductDTO {

    private Long id;
    private String name;
    private Double price;
    private String size;
    private String color;
    private Integer stock;
    private String image;
    private String status;
    private String description;

    private Long categoryId;
    private String categoryName;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Double price,
                      String size, String color, Integer stock,
                      String image, String status, String description,
                      Long categoryId, String categoryName) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.size = size;
        this.color = color;
        this.stock = stock;
        this.image = image;
        this.status = status;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // ===== GETTER SETTER =====


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}