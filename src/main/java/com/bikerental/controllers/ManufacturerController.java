package com.bikerental.controllers;

import com.bikerental.models.Manufacturer;
import com.bikerental.services.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@CrossOrigin(origins = "http://localhost:3000")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerService.getAllManufacturers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable Long id) {
        return ResponseEntity.ok(manufacturerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        return ResponseEntity.ok(manufacturerService.save(manufacturer));
    }
} 