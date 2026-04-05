package com.example.banquanao.controller;

import com.example.banquanao.dto.ProductDTO;
import com.example.banquanao.model.Category;
import com.example.banquanao.model.Color;
import com.example.banquanao.model.Product;
import com.example.banquanao.model.Size;
import com.example.banquanao.repository.CategoryRepository;
import com.example.banquanao.repository.ColorRepository;
import com.example.banquanao.repository.ProductRepository;
import com.example.banquanao.repository.SizeRepository;
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
    private final CategoryRepository categoryRepo;
    private final SizeRepository sizeRepo;
    private final ColorRepository colorRepo;

    public ProductController(ProductService service,
                             ProductRepository repo,
                             CategoryRepository categoryRepo,
                             SizeRepository sizeRepo,
                             ColorRepository colorRepo) {
        this.service = service;
        this.repo = repo;
        this.categoryRepo = categoryRepo;
        this.sizeRepo = sizeRepo;
        this.colorRepo = colorRepo;
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/active")
    public List<ProductDTO> getActive() {
        return service.getActive();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }
    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/upload")
    public ProductDTO createWithImage(
            @RequestParam("name") String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("sizeId") Long sizeId,
            @RequestParam("colorId") Long colorId,
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
        p.setStock(stock);
        p.setStatus(status);
        p.setDescription(description);
        p.setImage("http://localhost:8080/uploads/" + fileName);


        Category c = categoryRepo.findById(categoryId).orElse(null);
        p.setCategory(c);


        Size size = sizeRepo.findById(sizeId).orElse(null);
        p.setSize(size);


        Color color = colorRepo.findById(colorId).orElse(null);
        p.setColor(color);

        return service.createWithImage(p);
    }
}