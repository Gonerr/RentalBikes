package com.bikerental.models;

import jakarta.persistence.*;

@Entity
@Table(name = "characteristics")
public class CharacteristicsOfBicycles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "frame_material", nullable = false, length = 50)
    private String frameMaterial;  // Материал рамы

    @Column(name = "weight", nullable = false, precision = 4)
    private double weight;         // Вес велосипеда

    @Column(name = "number_of_speeds", nullable = false)
    private int numberOfSpeeds;    // Количество скоростей

    public CharacteristicsOfBicycles() {
    }

    public CharacteristicsOfBicycles(String frameMaterial, double weight, int numberOfSpeeds) {
        this.frameMaterial = frameMaterial;
        this.weight = weight;
        this.numberOfSpeeds = numberOfSpeeds;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrameMaterial() {
        return frameMaterial;
    }

    public void setFrameMaterial(String frameMaterial) {
        this.frameMaterial = frameMaterial;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getNumberOfSpeeds() {
        return numberOfSpeeds;
    }

    public void setNumberOfSpeeds(int numberOfSpeeds) {
        this.numberOfSpeeds = numberOfSpeeds;
    }
}
