package com.bikerental.controllers;

import com.bikerental.models.*;
import com.bikerental.services.BicycleService;
import com.bikerental.services.RentalService;
import com.bikerental.services.RentalPointService;
import com.bikerental.services.ManufacturerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bicycles")
@CrossOrigin(origins = "http://localhost:3000")
public class BicycleController {
    private final BicycleService bicycleService;
    private final RentalService rentalService;
    private final RentalPointService rentalPointService;
    private final ManufacturerService manufacturerService;

    public BicycleController(BicycleService bicycleService, 
                           RentalService rentalService,
                           RentalPointService rentalPointService,
                           ManufacturerService manufacturerService) {
        this.bicycleService = bicycleService;
        this.rentalService = rentalService;
        this.rentalPointService = rentalPointService;
        this.manufacturerService = manufacturerService;
    }

    @PostMapping
    public ResponseEntity<Bicycle> createBicycle(@RequestBody BicycleRequest request) {
        // Получаем производителя и пункт аренды
        Manufacturer manufacturer = manufacturerService.findById(request.getManufacturer().getId());
        RentalPoint rentalPoint = rentalPointService.findById(request.getRentalPoint());

        // Создаем характеристики велосипеда
        CharacteristicsOfBicycles characteristics = new CharacteristicsOfBicycles();
        characteristics.setFrameMaterial(request.getCharacteristics().getFrameMaterial());
        characteristics.setWeight(request.getCharacteristics().getWeight());
        characteristics.setNumberOfSpeeds(request.getCharacteristics().getNumberOfSpeeds());

        // Создаем велосипед нужного типа
        Bicycle bicycle;
        switch (request.getType()) {
            case "MOUNTAIN":
                MountainBicycle mountainBike = new MountainBicycle();
                mountainBike.setSuspensionType(request.getSuspensionType());
                mountainBike.setWheelSize(request.getWheelSize());
                bicycle = mountainBike;
                break;
            case "ROAD":
                RoadBicycle roadBike = new RoadBicycle();
                roadBike.setTireWidth(request.getTireWidth());
                roadBike.setAero(request.getIsAero());
                bicycle = roadBike;
                break;
            case "CITY":
                CityBicycle cityBike = new CityBicycle();
                cityBike.setHasBasket(request.getHasBasket());
                cityBike.setHasFenders(request.getHasFenders());
                cityBike.setHasBelt(request.getHasBelt());
                bicycle = cityBike;
                break;
            default:
                throw new IllegalArgumentException("Invalid bicycle type: " + request.getType());
        }

        // Устанавливаем общие свойства
        bicycle.setModel(request.getModel());
        bicycle.setManufacturer(manufacturer);
        bicycle.setRentalPoint(rentalPoint);
        bicycle.setCharacteristics(characteristics);
        bicycle.setPrice(request.getPrice());
        rentalPoint.addBicycle(bicycle);

        // Сохраняем велосипед
        return ResponseEntity.ok(bicycleService.save(bicycle));
    }

    @GetMapping
    public ResponseEntity<?> getAllBicycles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long rentalPoint,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Bicycle> bicycles = bicycleService.getAllBicycles();
            
            // Применяем все фильтры параллельно
            bicycles = bicycles.stream()
                .filter(b -> type == null || b.getBikeType().equals(type))
                .filter(b -> rentalPoint == null || b.getRentalPoint().getId().equals(rentalPoint))
                .filter(b -> minPrice == null || b.getDailyRentalPrice() >= minPrice)
                .filter(b -> maxPrice == null || b.getDailyRentalPrice() <= maxPrice)
                .filter(b -> {
                    if (availability == null) return true;
                    List<Rental> activeRentals = rentalService.getActiveRentalsForBicycle(b.getBicycle_id());
                    boolean isAvailable = activeRentals.isEmpty();
                    return availability.equals("available") ? isAvailable : !isAvailable;
                })
                .filter(b -> startDate == null || endDate == null || 
                    rentalService.getActiveRentalsForBicycle(b.getBicycle_id()).stream()
                        .allMatch(rental -> rental.isBicycleAvailableForPeriod(startDate, endDate)))
                .toList();
            
            return ResponseEntity.ok(bicycles);
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

    @GetMapping("/{id}/active-rentals")
    public ResponseEntity<List<Rental>> getActiveRentalsForBicycle(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getActiveRentalsForBicycle(id));
    }

    @GetMapping("/{id}/check-availability")
    public ResponseEntity<Boolean> checkBicycleAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Bicycle bicycle = bicycleService.findById(id);
        boolean isAvailable = rentalService.getActiveRentalsForBicycle(id).stream()
            .allMatch(rental -> rental.isBicycleAvailableForPeriod(startDate, endDate));
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/{id}/rent")
    public ResponseEntity<Bicycle> rentBicycle(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(bicycleService.rentBicycle(id, startDate, endDate));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Bicycle> returnBicycle(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime actualReturnDate) {
        return ResponseEntity.ok(bicycleService.returnBicycle(id, actualReturnDate));
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Service is UP!";
    }
}
