package com.bikerental.controllers;

import com.bikerental.models.RentalPoint;
import com.bikerental.services.RentalPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental-points")
@CrossOrigin(origins = "http://localhost:3000")
public class RentalPointController {
    private static final Logger log = LoggerFactory.getLogger(RentalPointController.class);
    private final RentalPointService rentalPointService;

    @Autowired
    public RentalPointController(RentalPointService rentalPointService) {
        this.rentalPointService = rentalPointService;
    }

    @GetMapping
    public ResponseEntity<List<RentalPoint>> getAllRentalPoints() {
        return ResponseEntity.ok(rentalPointService.getAllRentalPoints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalPoint> getRentalPointById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalPointService.findById(id));
    }
} 