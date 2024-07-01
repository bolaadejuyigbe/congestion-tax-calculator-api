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

import com.example.congestion_tax_calculator_api.model.VehicleType;
import com.example.congestion_tax_calculator_api.payload.response.GenericResponse;
import com.example.congestion_tax_calculator_api.service.Impl.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/vehicletypes")
@Tag(name = "VehicleTypes", description = "API for managing vehicles")

public class VehicleController {
 @Autowired
 private VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Get all vehicletypes")
    public CompletableFuture<ResponseEntity<GenericResponse<List<VehicleType>>>> getAllVehicles() {
       return vehicleService.getAllVehicleTypes()
                .thenApply(vehicletypes -> ResponseEntity.ok(GenericResponse.success(vehicletypes)))
                .exceptionally(ex -> {
                    return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
                });
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicletypes Id")
    public CompletableFuture<ResponseEntity<GenericResponse<Optional<VehicleType>>>>getVehicleById(@PathVariable int id) {
        return vehicleService.getVehicleTypeById(id)
        .thenApplyAsync(vehicleType -> ResponseEntity.ok(GenericResponse.success(vehicleType)))
        .exceptionally(ex -> {
         return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
      });
    }
}
