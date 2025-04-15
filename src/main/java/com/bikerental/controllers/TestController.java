package com.bikerental.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> Test() {
        return ResponseEntity.ok("Test OK"); // временно
    }

    @GetMapping("/bikes")
    public List<Map<String, String>> testBikes() {
        return List.of(
                Map.of("id", "1", "name", "Test Bike"),
                Map.of("id", "2", "name", "Another Bike")
        );
    }
}