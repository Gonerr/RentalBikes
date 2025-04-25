package com.bikerental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "client_rented_bicycles",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "bicycle_id")
    )
    @JsonIgnore
    private List<Bicycle> rentedBicycles = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Rental> rentalHistory = new ArrayList<>();

    public Client() {
    }

    public Client(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Bicycle> getRentedBicycles() {
        return new ArrayList<>(rentedBicycles);
    }

    public void setRentedBicycles(List<Bicycle> rentedBicycles) {
        this.rentedBicycles = new ArrayList<>(rentedBicycles);
    }

    public List<Rental> getRentalHistory() {
        return new ArrayList<>(rentalHistory);
    }

    // Методы аренды
    @Transactional
    public void rentBicycle(Bicycle bicycle, LocalDateTime endDate) {
        if (bicycle == null ||  endDate == null) {
            throw new IllegalArgumentException("Bicycle and end date cannot be null");
        }
        
        if (endDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("End date cannot be in the past");
        }

        // Проверяем, не арендован ли уже велосипед
        if (rentedBicycles.contains(bicycle)) {
            throw new IllegalStateException("Bicycle is already rented by this client");
        }

        // Проверяем, нет ли активной аренды этого велосипеда
        boolean isBicycleAvailable = rentalHistory.stream()
                .filter(r -> r.getBicycle().equals(bicycle))
                .allMatch(r -> r.getActualReturnDate() != null);
        
        if (!isBicycleAvailable) {
            throw new IllegalStateException("Bicycle is currently rented by another client");
        }

        rentedBicycles.add(bicycle);

        Rental rental = new Rental();
        rental.setClient(this);
        rental.setBicycle(bicycle);
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(endDate);
        rental.setTotalCost(rental.calculateCost());

        rentalHistory.add(rental);
    }

    @Transactional
    public void returnBicycle(Bicycle bicycle) {
        if (bicycle == null) {
            throw new IllegalArgumentException("Bicycle cannot be null");
        }

        if (!rentedBicycles.contains(bicycle)) {
            throw new IllegalStateException("This bicycle is not rented by this client");
        }

        rentedBicycles.remove(bicycle);

        // Находим активную аренду для этого велосипеда
        rentalHistory.stream()
                .filter(r -> r.getBicycle().equals(bicycle) && r.getActualReturnDate() == null)
                .findFirst()
                .ifPresent(r -> {
                    r.setActualReturnDate(LocalDateTime.now());
                    // Пересчитываем стоимость с учетом фактического времени возврата
                    if (r.getActualReturnDate().isAfter(r.getEndDate())) {
                        r.setTotalCost(r.calculateCost());
                    }
                });
    }

    // Методы для работы с арендами
    public void addRental(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental cannot be null");
        }
        
        if (!rentalHistory.contains(rental)) {
            rentalHistory.add(rental);
            rental.setClient(this);
            
            // Если это активная аренда, добавляем велосипед в список арендованных
            if (rental.getActualReturnDate() == null) {
                rentedBicycles.add(rental.getBicycle());
            }
        }
    }

    public void removeRental(Rental rental) {
        if (rental != null) {
            rentalHistory.remove(rental);
            rentedBicycles.remove(rental.getBicycle());
            rental.setClient(null);
        }
    }

    // Проверка наличия активной аренды
    public boolean hasActiveRental() {
        return !rentedBicycles.isEmpty();
    }

    // Получение активных аренд
    @JsonIgnore
    public List<Rental> getActiveRentals() {
        return rentalHistory.stream()
                .filter(r -> r.getActualReturnDate() == null)
                .toList();
    }

    // Проверка, арендован ли конкретный велосипед
    public boolean hasRentedBicycle(Bicycle bicycle) {
        return rentedBicycles.contains(bicycle);
    }
}
