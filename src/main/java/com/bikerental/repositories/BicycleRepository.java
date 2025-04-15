package com.bikerental.repositories;

import com.bikerental.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
    @Query("SELECT b FROM Bicycle b WHERE b.isAvailable = true")
    List<Bicycle> findAvailableBicycles();  // Найти все доступные велосипеды
    List<Bicycle> findByCharacteristics_Type(String type);  // Поиск по типу
    List<Bicycle> findByManufacturer(Manufacturer manufacturer);  // Поиск по производителю

    @Query("SELECT b FROM Bicycle b WHERE b.bikeType = :type")
    List<Bicycle> findByBikeType(@Param("type") String type);

    @Query("SELECT b FROM Bicycle b WHERE b.bicycle_id = :id")
    Optional<Bicycle> findByIdCustom(@Param("id") Long id);
}
