package com.example.banquanao.controller;

import com.example.banquanao.dto.ProductDTO;
import com.example.banquanao.model.Category;
import com.example.banquanao.model.Product;
import com.example.banquanao.repository.CategoryRepository;
import com.example.banquanao.repository.ProductRepository;
import com.example.banquanao.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;

    private final ProductRepository repo;

     CategoryRepository categoryRepo;

    public ProductController(ProductService service, ProductRepository repo, CategoryRepository categoryRepo) {
        this.service = service;
        this.repo = repo;
        this.categoryRepo = categoryRepo;
    }

    // GET ALL
    @GetMapping
    public List<ProductDTO> getAll() {
        return service.getAll();
    }

    // GET ACTIVE
    @GetMapping("/active")
    public List<ProductDTO> getActive() {
        return service.getActive();
    }

    // CREATE
    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return service.update(id, dto);
    }

    // DELETE (SOFT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @PostMapping("/upload")
    public ProductDTO createWithImage(
            @RequestParam("name") String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("size") String size,
            @RequestParam("color") String color,
            @RequestParam("stock") Integer stock,
            @RequestParam("status") String status,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path path = Paths.get("uploads/" + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Product p = new Product();

        p.setName(name);
        p.setPrice(price);
        p.setSize(size);
        p.setColor(color);
        p.setStock(stock);
        p.setStatus(status);
        p.setDescription(description);
        p.setImage("http://localhost:8080/uploads/" + fileName);

        // ✅ FIX QUAN TRỌNG
        Category c = categoryRepo.findById(categoryId).orElse(null);
        p.setCategory(c);

        return service.createWithImage(p);
    }
}