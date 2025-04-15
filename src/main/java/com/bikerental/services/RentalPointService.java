package com.bikerental.services;

import com.bikerental.models.RentalPoint;
import com.bikerental.repositories.RentalPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalPointService {
    private final RentalPointRepository rentalPointRepository;

    public RentalPoint findById(Long id) {
        return rentalPointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental point not found"));
    }

    public List<RentalPoint> getAllRentalPoints() {
        return rentalPointRepository.findAll();
    }
}