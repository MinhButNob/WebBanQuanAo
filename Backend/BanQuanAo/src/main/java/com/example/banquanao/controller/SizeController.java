package com.example.banquanao.controller;

import com.example.banquanao.model.Size;
import com.example.banquanao.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@CrossOrigin("*")
public class SizeController {

    public final SizeService service;

    public SizeController(SizeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Size> getAll() {
        return service.findAll();
    }

    @PostMapping
    public Size save(@RequestBody Size size) {
        return service.save(size);
    }

    @PutMapping("/{id}")
    public Size update(@PathVariable Long id, @RequestBody Size size) {
        return service.update(id, size);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
