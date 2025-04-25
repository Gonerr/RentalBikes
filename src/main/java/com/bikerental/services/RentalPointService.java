package com.bikerental.services;

import com.bikerental.models.RentalPoint;
import com.bikerental.repositories.RentalPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalPointService {
    private static final Logger log = LoggerFactory.getLogger(RentalPointService.class);
    private final RentalPointRepository rentalPointRepository;

    @Autowired
    public RentalPointService(RentalPointRepository rentalPointRepository) {
        this.rentalPointRepository = rentalPointRepository;
    }

    public RentalPoint findById(Long id) {
        return rentalPointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental point not found"));
    }

    public List<RentalPoint> getAllRentalPoints() {
        return rentalPointRepository.findAll();
    }
}