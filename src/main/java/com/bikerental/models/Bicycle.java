package com.bikerental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bicycles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"model", "manufacturer_id"})
})

// стратегия наследования (Объединенное наследование)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bike_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public abstract class Bicycle {
    // автоинкрементный первичный ключ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bicycle_id;

    // поле берется из @DiscriminatorColumn (горный, шоссейный, городской)
    @Column(name = "bike_type", insertable = false, updatable = false)
    private String bikeType;

    // связь с производителем
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    @JsonIgnoreProperties({"bicycles", "hibernateLazyInitializer", "handler"})
    private Manufacturer manufacturer;

    // связь с характеристиками велосипеда
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "characteristics_id")
    private CharacteristicsOfBicycles characteristics;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_point_id")
    @JsonIgnoreProperties({"bicycles", "hibernateLazyInitializer", "handler"})
    private RentalPoint rentalPoint;
    
    // название модели велосипеда (у каждой компании свои модели) 
    @Column(nullable = false)
    private String model;

    // стоимость велосипеда
    @Column(name = "price")
    private double price;

    // стоимость аренды в день
    @Column(name = "daily_rental_price")
    private double dailyRentalPrice;

    @Transient
    private static int totalBicycles;

    protected Bicycle() {
        totalBicycles++;
    }

    public Bicycle(String model, Manufacturer manufacturer,
                   CharacteristicsOfBicycles characteristics) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.characteristics = characteristics;
        totalBicycles++;
    }

    // Геттеры и сеттеры
    public Long getBicycle_id() {
        return bicycle_id;
    }

    public void setBicycle_id(Long bicycle_id) {
        this.bicycle_id = bicycle_id;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public RentalPoint getRentalPoint() {
        return rentalPoint;
    }

    public void setRentalPoint(RentalPoint rentalPoint) {
        this.rentalPoint = rentalPoint;
    }

    public CharacteristicsOfBicycles getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(CharacteristicsOfBicycles characteristics) {
        this.characteristics = characteristics;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        updateDailyRentalPrice();
    }

    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    public void setDailyRentalPrice(double dailyRentalPrice) {
        this.dailyRentalPrice = dailyRentalPrice;
    }

    public static int getTotalBicycles() {
        return totalBicycles;
    }

    // Общие методы для всех велосипедов
    @JsonIgnore
    public abstract double getRemainingPiece();

    // Обновление стоимости аренды в час
    protected void updateDailyRentalPrice() {
        double baseRentalPrice = 500.0; // Минимальный тариф
        double bikeValueMultiplier = 0.01; // 1% от стоимости велосипеда
        this.dailyRentalPrice = baseRentalPrice + (getRemainingPiece() * bikeValueMultiplier);
    }

    // Проверка доступности велосипеда на период
    // public boolean isAvailableForPeriod(LocalDateTime start, LocalDateTime end) {
    //     return rentalPoints.stream()
    //             .flatMap(point -> point.getRentals().stream())
    //             .filter(Rental::isActive)
    //             .allMatch(rental -> rental.isBicycleAvailableForPeriod(start, end));
    // }
}

