package com.bikerental.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "manufacturers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false, length = 100)
    private String name;

    @Setter
    @Getter
    @Column(nullable = false, length = 100)
    private String country;

    public Manufacturer() {
    }

    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // Коэффициенты стоимости велосипедов взависимости от страны производителя
    public static double getCountryMultiplier(String country) {
        if (country == null) return 1.0;

        return switch (country.toLowerCase()) {
            case "германия", "germany" -> 1.3;
            case "сша", "usa" -> 1.25;
            case "япония", "japan" -> 1.2;
            case "китай", "china" -> 0.9;
            case "россия", "russia" -> 1.0;
            default -> 1.0;
        };
    }

    // Наценка производителя на горные велосипеды
    public static double getMountainBikePremium(String country) {
        if (country == null) return 0.0;

        return switch (country.toLowerCase()) {
            case "германия", "germany" -> 5000.0;
            case "сша", "usa" -> 4000.0;
            case "швейцария", "switzerland" -> 6000.0;
            default -> 3000.0;
        };
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
