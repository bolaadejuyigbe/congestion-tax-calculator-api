package com.example.congestion_tax_calculator_api.controller;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.congestion_tax_calculator_api.payload.request.CongestionTaxcalculationRequest;
import com.example.congestion_tax_calculator_api.payload.response.GenericResponse;
import com.example.congestion_tax_calculator_api.service.ICongestionCalculatorService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/congestiontaxcalculator")
@Tag(name = "CongestionTaxCalculator", description = "API for managing congestion tax calculator")

public class CoongestionTaxCalculatorController {

     @Autowired
    private ICongestionCalculatorService congestionTaxCalculatorService;

    @PostMapping
    public CompletableFuture<ResponseEntity<GenericResponse<BigDecimal>>> calculateTax
    (@RequestBody CongestionTaxcalculationRequest request) {
        return congestionTaxCalculatorService.calculateTaxAsync(request.getCityId(), request.getVehicleRegId(), request.getVehicleTypeId(), request.getEntryDates())
        .thenApplyAsync(city -> ResponseEntity.ok(GenericResponse.success(city)))
        .exceptionally(ex -> {
         return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
      });
    }
}
