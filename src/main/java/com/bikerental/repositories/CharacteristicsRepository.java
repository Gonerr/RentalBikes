package com.bikerental.repositories;

import com.bikerental.models.CharacteristicsOfBicycles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicsRepository extends JpaRepository<CharacteristicsOfBicycles, Long> {

    // List<CharacteristicsOfBicycles> findByType(String type);
}