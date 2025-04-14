package com.bikerental.models;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "contact_info", nullable = false, length = 100)
    private String contactInfo;

    @ManyToMany
    @JoinTable(
            name = "client_rented_bicycles",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "bicycle_id")
    )

    private List<Bicycle> rentedBicycles = new ArrayList<>();
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentalHistory = new ArrayList<>();

    public Client() {
    }


    // Геттеры и сеттеры
    public Long getId() { return id; }

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public String getContactInfo() { return contactInfo;}
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo;}
    public List<Bicycle> getRentedBicycles() { return new ArrayList<>(rentedBicycles);}
    public void setRentedBicycles(List<Bicycle> rentedBicycles) {this.rentedBicycles = new ArrayList<>(rentedBicycles);}

    // Методы аренды
    @Transactional
    public void rentBicycle(Bicycle bicycle, RentalPoint rentalPoint, LocalDateTime endDate) {
        if (bicycle.isAvailable()) {
            rentedBicycles.add(bicycle);
            bicycle.rent();
            rentalPoint.removeBicycle(bicycle);

            Rental rental = new Rental();
            rental.setClient(this);
            rental.setBicycle(bicycle);
            rental.setStartDate(LocalDateTime.now());
            rental.setEndDate(endDate);
            rental.setRentalPoint(rentalPoint);

            rentalHistory.add(rental);
        }
    }

    @Transactional
    public void returnBicycle(Bicycle bicycle, RentalPoint rentalPoint) {
        if (rentedBicycles.contains(bicycle)) {
            rentedBicycles.remove(bicycle);
            bicycle.returnBicycle();
            rentalPoint.addBicycle(bicycle);

            rentalHistory.stream()
                    .filter(r -> r.getBicycle().equals(bicycle) && r.isActive())
                    .findFirst()
                    .ifPresent(r -> r.setEndDate(LocalDateTime.now()));
        }
    }

    public List<Rental> getRentalHistory() {
        return new ArrayList<>(rentalHistory);
    }

}
