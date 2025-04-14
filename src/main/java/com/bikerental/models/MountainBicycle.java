package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Горный велосипед
@Setter
@Getter
@Entity

@DiscriminatorValue("MOUNTAIN")
@Table(name = "mountain_bikes")

public class MountainBicycle extends Bicycle {

    // Специфичные методы для горного велосипеда
    @Column(name = "suspension_type", nullable = false)
    private String suspensionType;  // Тип подвески

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

    @Override
    public double getRemainingPiece() {
        // Логика расчета для горного велосипеда
        return 1000.0; // пример значения
    }
}
