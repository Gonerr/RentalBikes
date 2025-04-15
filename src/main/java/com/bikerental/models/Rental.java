package com.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    // Устанавливаем фактическую дату возврата
    @Setter
    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate; // поле для фактической даты возврата

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