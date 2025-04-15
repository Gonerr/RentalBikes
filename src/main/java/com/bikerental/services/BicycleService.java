package com.bikerental.services;

import com.bikerental.models.Bicycle;
import com.bikerental.models.MountainBicycle;
import com.bikerental.repositories.BicycleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BicycleService {
    private final BicycleRepository bicycleRepository;

    public List<Bicycle> getAllBicycles() {
        try {
            List<Bicycle> bikes = bicycleRepository.findAll();
            if (bikes.isEmpty()) {
                // Возвращаем тестовые данные, если БД пуста
                MountainBicycle testBike = new MountainBicycle();
                testBike.setModel("Test Bike");
                return List.of(testBike);
            }
            return bikes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get bicycles", e);
        }
    }

    public List<Bicycle> getAvailableBikes() {
        try {
            List<Bicycle> bikes = bicycleRepository.findAvailableBicycles();
            return bikes;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving available bicycles",
                    e
            );
        }
    }

    public List<Bicycle> getBikesByType(String type) {
        return bicycleRepository.findByCharacteristics_Type(type);  // Исправленный метод
    }

    public Bicycle findById(Long id) {
        log.debug("Attempting to find bicycle with id: {}", id);
        try {
            return bicycleRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Bicycle not found with id: {}", id);
                        return new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Bicycle not found");
                    });
        } catch (Exception e) {
            log.error("Error finding bicycle with id: " + id, e);
            throw e;
        }
    }

    public Bicycle rentBicycle(Long id) {
        Bicycle bike = findById(id);
        if (!bike.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bicycle is already rented");
        }
        bike.setAvailable(false);
        return bicycleRepository.save(bike);
    }

    public Bicycle returnBicycle(Long id) {
        Bicycle bike = findById(id);
        bike.setAvailable(true);
        return bicycleRepository.save(bike);
    }
}