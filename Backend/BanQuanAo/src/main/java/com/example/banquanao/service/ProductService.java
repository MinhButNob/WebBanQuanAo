package com.example.banquanao.service;



import com.example.banquanao.dto.ProductDTO;
import com.example.banquanao.model.Product;
import com.example.banquanao.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<ProductDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getActive() {
        return repo.findByStatus("ACTIVE").stream().map(this::toDTO).collect(Collectors.toList());
    }


    public ProductDTO create(ProductDTO dto) {
        Product p = toEntity(dto);
        p.setStatus("ACTIVE");
        return toDTO(repo.save(p));
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product p = repo.findById(id).orElseThrow();

        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setCategory(dto.getCategory());
        p.setSize(dto.getSize());
        p.setColor(dto.getColor());
        p.setStock(dto.getStock());
        p.setImage(dto.getImage());
        p.setStatus(dto.getStatus());

        return toDTO(repo.save(p));
    }


    public void delete(Long id) {
        Product p = repo.findById(id).orElseThrow();
        p.setStatus("INACTIVE");
        repo.save(p);
    }


    private ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCategory(),
                p.getSize(),
                p.getColor(),
                p.getStock(),
                p.getImage(),
                p.getStatus()
        );
    }

    private Product toEntity(ProductDTO dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getCategory(),
                dto.getSize(),
                dto.getColor(),
                dto.getStock(),
                dto.getImage(),
                dto.getStatus()
        );
    }
}