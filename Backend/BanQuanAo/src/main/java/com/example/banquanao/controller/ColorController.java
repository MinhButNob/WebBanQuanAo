package com.example.banquanao.controller;

import com.example.banquanao.model.Color;
import com.example.banquanao.repository.ColorRepository;
import com.example.banquanao.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin("*")
public class ColorController {
    @Autowired
    private ColorRepository colorRepo;

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public List<Color> findAll() {
        return colorService.findAll();
    }

    @PostMapping
    public Color save(@RequestBody Color color) {
        return colorService.save(color);
    }

    @PutMapping("/{id}")
    public Color update(@PathVariable Long id, @RequestBody Color color) {
        return colorService.update(id,color);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        colorService.delete(id);
    }

}
