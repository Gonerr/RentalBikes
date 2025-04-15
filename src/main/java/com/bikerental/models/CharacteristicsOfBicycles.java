package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "characteristics_of_bicycles")

public class CharacteristicsOfBicycles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;        // Тип велосипеда

    @Column
    private double weight;      // Вес велосипеда

    @Column(name = "gear_count", nullable = false)
    private int gearCount;     // Количество передач

    @Column(name = "has_lights", nullable = false)
    private boolean hasLights;  // Наличие фонарика

    public CharacteristicsOfBicycles() {
    }

    public String getCharacteristics() {
        return "Type: " + type + ", Weight: " + weight +
                ", Gears: " + gearCount + ", Lights: " + hasLights;
    }

    public boolean getHasLights() { return hasLights;}
}
