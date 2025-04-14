package com.bikerental.repositories;

import com.bikerental.models.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
    List<Bicycle> findByIsAvailableTrue();
    List<Bicycle> findByCharacteristicsType(String type);
}