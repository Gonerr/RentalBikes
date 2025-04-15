package com.bikerental.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bicycles")

// стратегия наследования (Объединенное наследование)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bike_type")
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public abstract class Bicycle {
    // автоинкрементный первичный ключ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bicycle_id;

    // Это поле будет автоматически заполняться из @DiscriminatorColumn
    @Column(name = "bike_type", insertable = false, updatable = false)
    private String bikeType;

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

