package com.bikerental.services;

import com.bikerental.models.Bicycle;
import com.bikerental.models.MountainBicycle;
import com.bikerental.models.Rental;
import com.bikerental.repositories.BicycleRepository;
import com.bikerental.repositories.RentalRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BicycleService {
    private static final Logger log = LoggerFactory.getLogger(BicycleService.class);
    private final BicycleRepository bicycleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public BicycleService(BicycleRepository bicycleRepository, RentalRepository rentalRepository) {
        this.bicycleRepository = bicycleRepository;
        this.rentalRepository = rentalRepository;
    }

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
        return bicycleRepository.findByCharacteristics_Type(type);
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

    public Bicycle rentBicycle(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        Bicycle bike = findById(id);
        
        // Проверяем, нет ли активных аренд на этот период
        List<Rental> activeRentals = rentalRepository.findByBicycleIdAndActualReturnDateIsNull(id);
        boolean isAvailable = activeRentals.stream()
                .allMatch(rental -> rental.isBicycleAvailableForPeriod(startDate, endDate));
                
        if (!isAvailable) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bicycle is not available for the specified period");
        }
        
        return bike;
    }

    public Bicycle returnBicycle(Long id, LocalDateTime actualReturnDate) {
        Bicycle bike = findById(id);
        
        // Находим активную аренду для этого велосипеда
        Rental activeRental = rentalRepository.findByBicycleIdAndActualReturnDateIsNull(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "No active rental found for this bicycle"));
        
        // Устанавливаем фактическую дату возврата
        activeRental.setActualReturnDate(actualReturnDate);
        rentalRepository.save(activeRental);
        
        return bike;
    }

    public Bicycle save(Bicycle bicycle) {
        return bicycleRepository.save(bicycle);
    }
}