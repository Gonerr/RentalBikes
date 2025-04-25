package com.bikerental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "manufacturers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Bicycle> bicycles = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String country;

    public Manufacturer() {
    }

    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Методы для работы с велосипедами
    public List<Bicycle> getBicycles() {
        return new ArrayList<>(bicycles);
    }

    public void addBicycle(Bicycle bicycle) {
        if (bicycle != null && !bicycles.contains(bicycle)) {
            bicycles.add(bicycle);
            bicycle.setManufacturer(this);
        }
    }

    public void removeBicycle(Bicycle bicycle) {
        if (bicycle != null && bicycles.contains(bicycle)) {
            bicycles.remove(bicycle);
            bicycle.setManufacturer(null);
        }
    }

    public List<Bicycle> getBicyclesByType(String type) {
        return bicycles.stream()
                .filter(b -> b.getBikeType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public long getTotalBicycleCount() {
        return bicycles.size();
    }

    public boolean hasBicycle(Bicycle bicycle) {
        return bicycles.contains(bicycle);
    }

    public double getAverageBicyclePrice() {
        if (bicycles.isEmpty()) {
            return 0.0;
        }
        return bicycles.stream()
                .mapToDouble(Bicycle::getPrice)
                .average()
                .orElse(0.0);
    }

    // Коэффициенты стоимости велосипедов взависимости от страны производителя
    public static double getCountryMultiplier(String country) {
        if (country == null) return 1.0;

        return switch (country.toLowerCase()) {
            case "германия", "germany" -> 1.3;
            case "сша", "usa" -> 1.25;
            case "япония", "japan" -> 1.2;
            case "китай", "china" -> 0.9;
            case "россия", "russia" -> 1.0;
            default -> 1.0;
        };
    }

    // Наценка производителя на горные велосипеды
    public static double getMountainBikePremium(String country) {
        if (country == null) return 0.0;

        return switch (country.toLowerCase()) {
            case "германия", "germany" -> 5000.0;
            case "сша", "usa" -> 4000.0;
            case "швейцария", "switzerland" -> 6000.0;
            default -> 3000.0;
        };
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
