package com.example.congestion_tax_calculator_api.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.congestion_tax_calculator_api.payload.response.CityResponse;
import com.example.congestion_tax_calculator_api.payload.response.GenericResponse;
import com.example.congestion_tax_calculator_api.service.Impl.CityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/")
@Tag(name = "City", description = "API for managing cities")

public class CityController {
 
    private CityService cityService;

    public CityController( CityService cityService){
        this.cityService = cityService;
    }

 
  
 private static final Logger logger = LoggerFactory.getLogger(CityController.class);
    
    @GetMapping( value = "cities", produces = "application/json")
    @Operation(summary = "Get all cities")
    public ResponseEntity<GenericResponse<List<CityResponse>>> getAllCities() {
        try {
            List<CityResponse> cities = cityService.getAllCities().get();
            logger.info("Returning cities from controller: {}", cities);
            return ResponseEntity.ok(GenericResponse.success(cities));
        } catch (Exception ex) {
            logger.error("Error occurred: ", ex);
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("cities/{id}")
    @Operation(summary = "Get city by Id")
    public ResponseEntity<GenericResponse<CityResponse>> getCityById(@PathVariable int id) {
        try {
            Optional<CityResponse> city = cityService.getCityById(id).get();
            if (city.isPresent()) {
                logger.info("Returning city from controller: {}", city.get());
                return ResponseEntity.ok(GenericResponse.success(city.get()));
            } else {
                logger.warn("City not found with ID: {}", id);
                return ResponseEntity.status(404).body(GenericResponse.error("city not found"));
            }
        } catch (Exception ex) {
            logger.error("Error occurred: ", ex);
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
        }
    }
}
