package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bicycles")

// стратегия наследования (Объединенное наследование)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bike_type")
@Getter @Setter

public abstract class Bicycle {
    // автоинкрементный первичный ключ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // связь с производителем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "characteristics_id")
    private CharacteristicsOfBicycles characteristics;

    @Column(nullable = false)
    private String model;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    // Статическое поле не сохраняется в БД
    @Transient
    private static int totalBicycles;

    protected Bicycle() {}

    public Bicycle(String model, Manufacturer manufacturer,
                   CharacteristicsOfBicycles characteristics) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.characteristics = characteristics;
        this.isAvailable = true;
    }

    // Общие методы для всех велосипедов
    public abstract double getRemainingPiece();

    // Геттеры и сеттеры
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    // Забронировано
    public void rent() {
        isAvailable = false;
    }
    // Отмена брони
    public void returnBicycle() {
        isAvailable = true;
    }

    public int getTotalBicycles() {
        return totalBicycles;
    }

}

