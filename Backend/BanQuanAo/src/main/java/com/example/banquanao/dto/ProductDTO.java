package com.example.banquanao.dto;



public class ProductDTO {
    private Long id;
    private String name;
    private Double price;

    // ✅ Thay vì String size, String color
    private Long sizeId;
    private String sizeName;
    private Long colorId;
    private String colorName;

    private Integer stock;
    private String image;
    private String status;
    private String description;
    private Long categoryId;
    private String categoryName;

    // Constructor đầy đủ
    public ProductDTO(Long id, String name, Double price,
                      Long sizeId, String sizeName,
                      Long colorId, String colorName,
                      Integer stock, String image, String status,
                      String description, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.colorId = colorId;
        this.colorName = colorName;
        this.stock = stock;
        this.image = image;
        this.status = status;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Long getSizeId() { return sizeId; }
    public void setSizeId(Long sizeId) { this.sizeId = sizeId; }

    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }

    public Long getColorId() { return colorId; }
    public void setColorId(Long colorId) { this.colorId = colorId; }

    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

}