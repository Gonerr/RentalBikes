package com.bikerental.services;

import com.bikerental.models.*;
import com.bikerental.repositories.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final BicycleService bicycleService;
    private final ClientService clientService;
    private final RentalPointService rentalPointService;

    @Transactional
    public Rental rentBicycle(Long clientId, Long bicycleId, Long rentalPointId, LocalDateTime endDate) {
        Client client = clientService.findById(clientId);
        Bicycle bicycle = bicycleService.findById(bicycleId);
        RentalPoint rentalPoint = rentalPointService.findById(rentalPointId);

        if (!bicycle.isAvailable()) {
            throw new IllegalStateException("Bicycle is already rented");
        }

        Rental rental = new Rental();
        rental.setClient(client);
        rental.setBicycle(bicycle);
        rental.setRentalPoint(rentalPoint);
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(endDate);

        bicycle.setAvailable(false);
        rentalPoint.removeBicycle(bicycle);
        client.getRentedBicycles().add(bicycle);

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnBicycle(Long rentalId, Long returnPointId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        RentalPoint returnPoint = rentalPointService.findById(returnPointId);

        rental.setActualReturnDate(LocalDateTime.now());
        rental.setStatus("COMPLETED");

        Bicycle bicycle = rental.getBicycle();
        bicycle.setAvailable(true);
        returnPoint.addBicycle(bicycle);

        Client client = rental.getClient();
        client.getRentedBicycles().remove(bicycle);

        return rentalRepository.save(rental);
    }

    public List<Rental> getActiveRentals() {
        return rentalRepository.findActiveRentals();
    }

    @Scheduled(fixedRate = 3600000) // Проверка каждый час
    public void checkExpiredRentals() {
        List<Rental> expired = rentalRepository.findByEndDateBeforeAndActiveTrue(LocalDateTime.now());
        expired.forEach(rental -> {
            rental.getBicycle().setAvailable(true);
        });
        rentalRepository.saveAll(expired);
    }
}