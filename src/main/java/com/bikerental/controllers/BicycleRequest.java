package com.bikerental.controllers;

import com.bikerental.models.Manufacturer;
import com.bikerental.models.CharacteristicsOfBicycles;

public class BicycleRequest {
    private String type;
    private String model;
    private Manufacturer manufacturer;
    private CharacteristicsOfBicycles characteristics;
    private Long rentalPoint;
    private double price;
    
    // Горный велосипед
    private String suspensionType;
    private Double wheelSize;
    
    // Городской велосипед
    private Boolean hasBasket;
    private Boolean hasFenders;
    private Boolean hasBelt;
    
    // Шоссейный велосипед
    private Double tireWidth;
    private Boolean isAero;

    // Геттеры и сеттеры
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public CharacteristicsOfBicycles getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(CharacteristicsOfBicycles characteristics) {
        this.characteristics = characteristics;
    }

    public Long getRentalPoint() {
        return rentalPoint;
    }

    public void setRentalPoint(Long rentalPoint) {
        this.rentalPoint = rentalPoint;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSuspensionType() {
        return suspensionType;
    }

    public void setSuspensionType(String suspensionType) {
        this.suspensionType = suspensionType;
    }

    public Double getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(Double wheelSize) {
        this.wheelSize = wheelSize;
    }

    public Boolean getHasBasket() {
        return hasBasket;
    }

    public void setHasBasket(Boolean hasBasket) {
        this.hasBasket = hasBasket;
    }

    public Boolean getHasFenders() {
        return hasFenders;
    }

    public void setHasFenders(Boolean hasFenders) {
        this.hasFenders = hasFenders;
    }

    public Boolean getHasBelt() {
        return hasBelt;
    }

    public void setHasBelt(Boolean hasBelt) {
        this.hasBelt = hasBelt;
    }

    public Double getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(Double tireWidth) {
        this.tireWidth = tireWidth;
    }

    public Boolean getIsAero() {
        return isAero;
    }

    public void setIsAero(Boolean isAero) {
        this.isAero = isAero;
    }
} 