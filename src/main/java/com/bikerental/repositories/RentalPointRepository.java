package com.bikerental.repositories;

import com.bikerental.models.RentalPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalPointRepository extends JpaRepository<RentalPoint, Long> {

}