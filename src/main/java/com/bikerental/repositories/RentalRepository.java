package com.bikerental.repositories;

import com.bikerental.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.actualReturnDate IS NULL")
    List<Rental> findByActualReturnDateIsNull();

    @Query("SELECT r FROM Rental r WHERE r.bicycle.bicycle_id = :bicycleId AND r.actualReturnDate IS NULL")
    List<Rental> findByBicycleIdAndActualReturnDateIsNull(@Param("bicycleId") Long bicycleId);

    @Query("SELECT r FROM Rental r WHERE r.endDate < :currentDate AND r.actualReturnDate IS NULL")
    List<Rental> findByEndDateBeforeAndActualReturnDateIsNull(@Param("currentDate") LocalDateTime currentDate);
}