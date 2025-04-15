package com.bikerental.controllers;

import com.bikerental.models.Bicycle;
import com.bikerental.services.BicycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicycles")
@RequiredArgsConstructor
public class BicycleController {
    private final BicycleService bicycleService;

    @GetMapping
    public ResponseEntity<?> getAllBicycles() {
        try {
            return ResponseEntity.ok(bicycleService.getAllBicycles());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Bicycle>> getAvailableBicycles() {
        return ResponseEntity.ok(bicycleService.getAvailableBikes());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Bicycle>> getBicyclesByType(@PathVariable String type) {
        return ResponseEntity.ok(bicycleService.getBikesByType(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bicycle> getBicycleById(@PathVariable Long id) {
        return ResponseEntity.ok(bicycleService.findById(id));
    }

    @PostMapping("/{id}/rent")
    public ResponseEntity<Bicycle> rentBicycle(@PathVariable Long id) {
        return ResponseEntity.ok(bicycleService.rentBicycle(id));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Bicycle> returnBicycle(@PathVariable Long id) {
        return ResponseEntity.ok(bicycleService.returnBicycle(id));
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Service is UP!";
    }
}

