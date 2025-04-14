package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "rentals")
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bicycle_id", nullable = false)
    private Bicycle bicycle;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_point_id", nullable = false)
    private RentalPoint rentalPoint;

    @Column(name = "actual_end_date")
    private LocalDateTime actualEndDate;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, CANCELLED

    public Rental() {
    }

    public double calculateCost() {
        if (startDate == null || endDate == null) return 0;
        long hours = ChronoUnit.HOURS.between(startDate, endDate);
        // Базовая ставка 200 руб./час
        return hours * 200.0;
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}