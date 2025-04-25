package com.bikerental.repositories;

import com.bikerental.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
    
    @Query("SELECT b FROM Bicycle b WHERE NOT EXISTS " +
           "(SELECT r FROM Rental r WHERE r.bicycle = b AND " +
           "r.startDate <= CURRENT_TIMESTAMP AND r.endDate >= CURRENT_TIMESTAMP)")
    List<Bicycle> findAvailableBicycles();  // Найти все доступные велосипеды на текущий день

    @Query("SELECT b FROM Bicycle b WHERE b.bikeType = :type")
    List<Bicycle> findByCharacteristics_Type(@Param("type") String type);  // Поиск по типу велосипеда

    List<Bicycle> findByManufacturer(Manufacturer manufacturer);  // Поиск по производителю

    @Query("SELECT b FROM Bicycle b WHERE b.bicycle_id = :id")
    Optional<Bicycle> findByIdCustom(@Param("id") Long id);
}
