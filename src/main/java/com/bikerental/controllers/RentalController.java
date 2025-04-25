package com.bikerental.controllers;

import com.bikerental.models.Rental;
import com.bikerental.models.Client;
import com.bikerental.services.RentalService;
import com.bikerental.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:3000")
public class RentalController {
    private static final Logger log = LoggerFactory.getLogger(RentalController.class);
    private final RentalService rentalService;
    private final ClientService clientService;

    @Autowired
    public RentalController(RentalService rentalService, ClientService clientService) {
        this.rentalService = rentalService;
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestBody RentalRequest request) {
        // Проверяем, существует ли клиент с таким телефоном
        Client client = clientService.findByPhone(request.getClientPhone());
        
        if (client == null) {
            // Если клиент не существует, создаем нового
            client = new Client();
            client.setName(request.getClientName());
            client.setEmail(request.getClientEmail());
            client.setPhone(request.getClientPhone());
            client = clientService.save(client);
        }

        // Создаем аренду с ID клиента
        return ResponseEntity.ok(rentalService.rentBicycle(
            client.getId(),
            request.getBicycleId(),
            request.getStartDate(),
            request.getEndDate()
        ));
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getActiveRentals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Rental> returnBicycle(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.returnBicycle(id));
    }
}

class RentalRequest {
    private Long bicycleId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Геттеры и сеттеры
    public Long getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(Long bicycleId) {
        this.bicycleId = bicycleId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
