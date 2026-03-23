package com.example.banquanao.controller;

import com.example.banquanao.dto.ProductDTO;
import com.example.banquanao.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
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
}