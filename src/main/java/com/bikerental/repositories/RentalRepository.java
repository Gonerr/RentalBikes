package com.bikerental.repositories;

import com.bikerental.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByStatus(String status);

//    @Query("SELECT r FROM Rental r WHERE r.status = :status AND r.bicycle.isAvailable = true")
//    List<Rental> findByStatusAndAvailableTrue(@Param("status") String status);       // Поиск активных велосипедов
    @Query("SELECT r FROM Rental r WHERE r.endDate > CURRENT_TIMESTAMP AND r.bicycle.isAvailable = false")
    List<Rental> findActiveRentals();

    // Поиск просроченных активных аренд (где endDate уже прошел, но аренда еще активна)
    @Query("SELECT r FROM Rental r WHERE r.endDate < :currentDate AND r.bicycle.isAvailable = false")
    List<Rental> findByEndDateBeforeAndActiveTrue(@Param("currentDate") LocalDateTime currentDate);
}