package com.example.banquanao.service;

import com.example.banquanao.model.Size;
import com.example.banquanao.repository.SizeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SizeService {
    private final SizeRepository sizeRepo;
    public SizeService(SizeRepository sizeRepo) {
        this.sizeRepo = sizeRepo;
    }

    public List<Size> findAll() {
        return sizeRepo.findAll();
    }
    public Size save(Size size) {
        return sizeRepo.save(size);
    }
    public Size findById(Long id) {
        return sizeRepo.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        Size size = sizeRepo.findById(id).orElseThrow();
        size.setStatus("INACTIVE");
        sizeRepo.save(size);
    }
    public Size update(Long id,Size size) {
        Size c = sizeRepo.findById(id).orElseThrow();
        c.setSizeName(size.getSizeName());
        c.setStatus(size.getStatus());
        return sizeRepo.save(c);
    }
}
