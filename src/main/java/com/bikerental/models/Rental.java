package com.bikerental.models;

import jakarta.persistence.*;
import org.springframework.scheduling.annotation.Scheduled;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "rentals")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnoreProperties({"rentedBicycles", "rentalHistory", "activeRentals", "hibernateLazyInitializer", "handler"})
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bicycle_id", nullable = false)
    @JsonIgnoreProperties({"rentals", "clients", "hibernateLazyInitializer", "handler"})
    private Bicycle bicycle;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;

    @Column(name = "total_cost")
    private Double totalCost;

    public Rental() {
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bicycle getBicycle() {
        return bicycle;
    }

    public void setBicycle(Bicycle bicycle) {
        this.bicycle = bicycle;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    // Расчет стоимости аренды за сутки
    public double calculateCost() {
        if (startDate == null || endDate == null) return 0;
        
        // Рассчитываем количество дней аренды
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days < 1) days = 1; // Минимальный период аренды - 1 день
        
        return days * bicycle.getDailyRentalPrice();
    }

    // Проверка, доступен ли велосипед для аренды в указанный период
    public boolean isBicycleAvailableForPeriod(LocalDateTime start, LocalDateTime end) {
        // Проверяем, не пересекается ли период с текущей арендой
        return end.isBefore(startDate) || start.isAfter(endDate);
    }

    // Проверка доступности велосипеда на период
    public static boolean isBicycleAvailableForPeriod(List<Rental> activeRentals, Long bicycleId, LocalDateTime start, LocalDateTime end) {
        return activeRentals.stream()
                .filter(rental -> rental.getBicycle().getBicycle_id().equals(bicycleId))
                .allMatch(rental -> rental.isBicycleAvailableForPeriod(start, end));
    }
}