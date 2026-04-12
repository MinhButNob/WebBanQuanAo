package com.example.banquanao.service;

import com.example.banquanao.model.Color;
import com.example.banquanao.repository.ColorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {
    private final ColorRepository colorRepo;
    public ColorService(ColorRepository colorRepo) {
        this.colorRepo = colorRepo;
    }

    public List<Color> findAll() {
        return colorRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Color findById(Long id) {
        return colorRepo.findById(id).orElse(null);
    }

    public Color save(Color color) {
        return colorRepo.save(color);
    }

    public Color update(Long id, Color color) {
        Color c = colorRepo.findById(color.getId()).orElseThrow();
        c.setColorName(color.getColorName());
        c.setStatus(color.getStatus());
        return colorRepo.save(c);
    }


    public void delete(Long id) {
        Color c = colorRepo.findById(id).orElseThrow();
        c.setStatus("INACTIVE");
        colorRepo.save(c);
    }

}
