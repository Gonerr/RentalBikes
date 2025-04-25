package com.bikerental.services;

import com.bikerental.models.*;
import com.bikerental.repositories.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {
    private static final Logger log = LoggerFactory.getLogger(RentalService.class);
    private final RentalRepository rentalRepository;
    private final BicycleService bicycleService;
    private final ClientService clientService;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                        BicycleService bicycleService,
                        ClientService clientService) {
        this.rentalRepository = rentalRepository;
        this.bicycleService = bicycleService;
        this.clientService = clientService;
    }

    @Transactional
    public Rental rentBicycle(Long clientId, Long bicycleId, LocalDateTime startDate, LocalDateTime endDate) {
        Client client = clientService.findById(clientId);
        Bicycle bicycle = bicycleService.findById(bicycleId);

        // Проверяем, нет ли активных аренд на этот период
        List<Rental> activeRentals = rentalRepository.findByBicycleIdAndActualReturnDateIsNull(bicycleId);
        boolean isAvailable = activeRentals.stream()
                .allMatch(rental -> rental.isBicycleAvailableForPeriod(startDate, endDate));

        if (!isAvailable) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bicycle is not available for the specified period");
        }

        Rental rental = new Rental();
        rental.setClient(client);
        rental.setBicycle(bicycle);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalCost(rental.calculateCost());

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnBicycle(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rental not found"));

        rental.setActualReturnDate(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    public List<Rental> getActiveRentals() {
        return rentalRepository.findByActualReturnDateIsNull();
    }

    public List<Rental> getActiveRentalsForBicycle(Long bicycleId) {
        return rentalRepository.findByBicycleIdAndActualReturnDateIsNull(bicycleId);
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rental not found"));
    }

    @Scheduled(fixedRate = 3600000) // Проверка каждый час
    public void checkExpiredRentals() {
        List<Rental> expired = rentalRepository.findByEndDateBeforeAndActualReturnDateIsNull(LocalDateTime.now());
        expired.forEach(rental -> {
            // Можно добавить логику для обработки просроченных аренд
            log.warn("Rental {} is expired but not returned", rental.getId());
        });
    }
}