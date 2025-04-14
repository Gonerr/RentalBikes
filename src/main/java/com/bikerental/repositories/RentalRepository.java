package com.bikerental.repositories;

import com.bikerental.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByClientId(Long clientId);
    List<Rental> findByStatus(String status);
}