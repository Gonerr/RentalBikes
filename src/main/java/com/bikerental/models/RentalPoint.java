package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "rental_points")
@Getter @Setter
public class RentalPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String location;

    @ManyToMany
    @JoinTable(
            name = "rental_point_bicycles",
            joinColumns = @JoinColumn(name = "rental_point_id"),
            inverseJoinColumns = @JoinColumn(name = "bicycle_id")
    )
    private List<Bicycle> availableBicycles = new ArrayList<>();

    @OneToMany(mappedBy = "rentalPoint")
    private List<Rental> rentals = new ArrayList<>();

    public RentalPoint() {
    }

    public List<Bicycle> getAvailableBicycles() {return new ArrayList<>(availableBicycles);}
    public void setAvailableBicycles(List<Bicycle> availableBicycles) {this.availableBicycles = new ArrayList<>(availableBicycles);}

    // Методы управления велосипедами
    public void addBicycle(Bicycle bicycle) {
        if (bicycle != null && !availableBicycles.contains(bicycle)) {
            availableBicycles.add(bicycle);
            bicycle.setAvailable(true);
        }
    }

    public void removeBicycle(Bicycle bicycle) {
        availableBicycles.remove(bicycle);
        bicycle.setAvailable(false);
    }

    // Поиск доступных велосипедов по типу
    public List<Bicycle> findAvailableByType(String type) {
        return availableBicycles.stream()
                .filter(b -> b.getCharacteristics().getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
}
