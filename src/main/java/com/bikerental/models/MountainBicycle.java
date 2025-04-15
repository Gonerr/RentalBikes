package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Горный велосипед
@Setter
@Getter
@Entity

@DiscriminatorValue("MOUNTAIN")
@Table(name = "mountain_bicycles")
@PrimaryKeyJoinColumn(name = "bicycle_id")

public class MountainBicycle extends Bicycle {

    private static final double BASE_PRICE = 25000.0;

    @Column(name = "suspension_type", nullable = false)
    private String suspensionType = "full";  // Тип подвески

    @Column(name = "wheel_size", nullable = false, precision = 4)
    private double wheelSize;       // Размер колеса

    public MountainBicycle() {
    }

    public MountainBicycle(String model, Manufacturer manufacturer,
                           CharacteristicsOfBicycles characteristics,
                           String suspensionType, double wheelSize) {
        super(model, manufacturer, characteristics);
        this.suspensionType = suspensionType;
        this.wheelSize = wheelSize;
    }

    public String getSuspensionType() {
        return suspensionType != null ? suspensionType.toLowerCase() : null;
    }

    @Override
    public double getRemainingPiece() {
        double price = BASE_PRICE;

        // Наценка за тип подвески
        switch (suspensionType.toLowerCase()) {
            case "full" -> price += 8000.0;
            case "front" -> price += 5000.0;
            case "hardtail" -> price += 3000.0;
        }

        // Наценка за размер колес
        if (wheelSize > 27.5) {
            price += 2000.0; // Крупные колеса дороже
        }

        // Премиум за производителя (горные велосипеды от известных производителей ценятся выше)
        double countryPremium = Manufacturer.getMountainBikePremium(getManufacturer().getCountry());
        price += countryPremium;
        double countryMultiplier = Manufacturer.getCountryMultiplier(getManufacturer().getCountry());
        return price * countryMultiplier;
    }
}
