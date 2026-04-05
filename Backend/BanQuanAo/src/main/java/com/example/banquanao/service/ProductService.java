package com.example.banquanao.service;



import com.example.banquanao.dto.ProductDTO;
import com.example.banquanao.model.Color;
import com.example.banquanao.model.Product;
import com.example.banquanao.model.Size;
import com.example.banquanao.repository.CategoryRepository;
import com.example.banquanao.repository.ColorRepository;
import com.example.banquanao.repository.ProductRepository;
import com.example.banquanao.repository.SizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;
    private final SizeRepository sizeRepo;      // ✅ Thêm
    private final ColorRepository colorRepo;    // ✅ Thêm

    public ProductService(ProductRepository repo,
                          CategoryRepository categoryRepo,
                          SizeRepository sizeRepo,      // ✅ Thêm
                          ColorRepository colorRepo) {  // ✅ Thêm
        this.repo = repo;
        this.categoryRepo = categoryRepo;
        this.sizeRepo = sizeRepo;
        this.colorRepo = colorRepo;
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
        p.setStock(dto.getStock());
        p.setImage(dto.getImage());
        p.setStatus(dto.getStatus());
        p.setDescription(dto.getDescription());

        // ✅ SỬA: set Size từ ID
        if (dto.getSizeId() != null) {
            Size size = sizeRepo.findById(dto.getSizeId()).orElse(null);
            p.setSize(size);
        }

        // ✅ SỬA: set Color từ ID
        if (dto.getColorId() != null) {
            Color color = colorRepo.findById(dto.getColorId()).orElse(null);
            p.setColor(color);
        }

        if (dto.getCategoryId() != null) {
            p.setCategory(categoryRepo.findById(dto.getCategoryId()).orElse(null));
        }

        return toDTO(repo.save(p));
    }

    public ProductDTO getById(Long id) {
        Product product = repo.findById(id).orElseThrow();
        return toDTO(product);
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
                p.getSize() != null ? p.getSize().getId() : null,     // ✅ sizeId
                p.getSize() != null ? p.getSize().getSizeName() : null, // ✅ sizeName
                p.getColor() != null ? p.getColor().getId() : null,    // ✅ colorId
                p.getColor() != null ? p.getColor().getColorName() : null, // ✅ colorName
                p.getStock(),
                p.getImage(),
                p.getStatus(),
                p.getDescription(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null
        );
    }

    public ProductDTO createWithImage(Product p) {
        return toDTO(repo.save(p));
    }


    private Product toEntity(ProductDTO dto) {
        Product p = new Product();

        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setImage(dto.getImage());
        p.setStatus(dto.getStatus());
        p.setDescription(dto.getDescription());

        // ✅ SỬA: set Size từ ID
        if (dto.getSizeId() != null) {
            Size size = sizeRepo.findById(dto.getSizeId()).orElse(null);
            p.setSize(size);
        }

        // ✅ SỬA: set Color từ ID
        if (dto.getColorId() != null) {
            Color color = colorRepo.findById(dto.getColorId()).orElse(null);
            p.setColor(color);
        }

        if (dto.getCategoryId() != null) {
            p.setCategory(categoryRepo.findById(dto.getCategoryId()).orElse(null));
        }

        return p;
    }


}