package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "bicycle_characteristics")


public class CharacteristicsOfBicycles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "type", nullable = false, length = 50)
    private String type;        // Тип велосипеда

    @Getter
    @Column
    private double weight;      // Вес велосипеда

    @Getter
    @Column(name = "gear_count", nullable = false)
    private int gearCount;     // Количество передач

    @Column(name = "has_lights", nullable = false)
    private boolean hasLights;  // Наличие фонарика

    public CharacteristicsOfBicycles() {
    }

    // Геттеры и сеттеры
    public String getCharacteristics() {
        return "Type: " + type + ", Weight: " + weight +
                ", Gears: " + gearCount + ", Lights: " + hasLights;
    }

    public boolean getHasLights() { return hasLights;}
}
