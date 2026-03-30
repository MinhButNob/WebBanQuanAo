package com.example.banquanao.service;

import com.example.banquanao.model.Category;
import com.example.banquanao.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }
    public List<Category> findAll() {
        return repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Category findById(Long id) {
        return repo.findById(id).orElseThrow();
    }


    public Category save(Category category) {
        return repo.save(category);
    }

    public Category update(Long id,Category newCategory) {
    Category category = repo.findById(id).orElseThrow();
    category.setName(newCategory.getName());
    category.setDescription(newCategory.getDescription());
    category.setStatus(newCategory.getStatus());
    return repo.save(category);
    }
    public void delete(Long id) {
        Category category = repo.findById(id).orElseThrow();
        category.setStatus("INACTIVE");
        repo.save(category);
    }
}
