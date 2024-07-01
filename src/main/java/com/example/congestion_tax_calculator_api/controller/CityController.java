package com.example.congestion_tax_calculator_api.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.congestion_tax_calculator_api.model.City;
import com.example.congestion_tax_calculator_api.payload.response.GenericResponse;
import com.example.congestion_tax_calculator_api.service.Impl.CityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cities")
@Tag(name = "City", description = "API for managing cities")

public class CityController {
 @Autowired
 private CityService cityService;

    @GetMapping
    @Operation(summary = "Get all cities")
    public CompletableFuture<ResponseEntity<GenericResponse<List<City>>>> getAllCities() {
        return cityService.getAllCities()
                .thenApply(cities -> ResponseEntity.ok(GenericResponse.success(cities)))
                .exceptionally(ex -> {
                    return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
                });
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get city by Id")
    public CompletableFuture<ResponseEntity<GenericResponse<Optional<City>>>> getCityById(@PathVariable int id) {
        return cityService.getCityById(id)
              .thenApplyAsync(city -> ResponseEntity.ok(GenericResponse.success(city)))
              .exceptionally(ex -> {
               return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
            });
    }
}
