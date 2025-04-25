package com.bikerental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "rental_points")
public class RentalPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String building;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "rentalPoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Bicycle> bicycles = new ArrayList<>();

    public RentalPoint() {
    }

    public RentalPoint(String location, String street, String building, String phone) {
        this.location = location;
        this.street = street;
        this.building = building;
        this.phone = phone;
    }

    // Геттеры и сеттеры для основных полей
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Методы для работы с велосипедами
    public List<Bicycle> getBicycles() {
        return new ArrayList<>(bicycles);
    }

    public void addBicycle(Bicycle bicycle) {
        if (bicycle != null && !bicycles.contains(bicycle)) {
            bicycles.add(bicycle);
            bicycle.setRentalPoint(this);
        }
    }

    public void removeBicycle(Bicycle bicycle) {
        if (bicycle != null && bicycles.contains(bicycle)) {
            bicycles.remove(bicycle);
            bicycle.setRentalPoint(null);
        }
    }

    public boolean hasBicycle(Bicycle bicycle) {
        return bicycles.contains(bicycle);
    }

    public long getTotalBicycleCount() {
        return bicycles.size();
    }

    // Получение полного адреса
    public String getFullAddress() {
        return String.format("%s, %s, %s", location, street, building);
    }
}
