package com.bikerental.controllers;

import com.bikerental.models.Rental;
import com.bikerental.services.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentBicycle(
            @RequestParam Long clientId,
            @RequestParam Long bicycleId,
            @RequestParam Long rentalPointId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(rentalService.rentBicycle(clientId, bicycleId, rentalPointId, endDate));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Rental> returnBicycle(
            @PathVariable Long id,
            @RequestParam Long returnPointId) {
        return ResponseEntity.ok(rentalService.returnBicycle(id, returnPointId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Rental>> getActiveRentals() {
        return ResponseEntity.ok(rentalService.getActiveRentals());
    }

}