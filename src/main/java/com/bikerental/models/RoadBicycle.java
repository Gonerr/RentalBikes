package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity

@DiscriminatorValue("ROAD")
@Table(name = "road_bikes")
// Шоссейный велосипед
public class RoadBicycle extends Bicycle {

    // Специфичные методы для шоссейного велосипеда
    @Setter
    @Getter
    @Column(name = "tire_width", nullable = false)
    private double tireWidth;   // Ширина шины

    @Column(name = "is_aero", nullable = false)
    private boolean isAero;     // Наличие аэродинамики

    public RoadBicycle() {
    }

    public RoadBicycle(String model, Manufacturer manufacturer,
                       CharacteristicsOfBicycles characteristics,
                       double tireWidth, boolean isAero) {
        super(model, manufacturer, characteristics);
        this.tireWidth = tireWidth;
        this.isAero = isAero;
    }

    public boolean isAero() { return isAero;}
    public void setAero(boolean aero) { isAero = aero; }

    @Override
    public double getRemainingPiece() {
        // Логика расчета для шоссейного велосипеда
        return 1200.0; // пример значения
    }
}
