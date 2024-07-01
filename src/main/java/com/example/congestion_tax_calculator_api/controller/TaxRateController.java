package com.example.congestion_tax_calculator_api.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.congestion_tax_calculator_api.map.MappingProfile;
import com.example.congestion_tax_calculator_api.model.TaxRate;
import com.example.congestion_tax_calculator_api.payload.request.TaxRateRequest;
import com.example.congestion_tax_calculator_api.payload.response.GenericResponse;
import com.example.congestion_tax_calculator_api.service.Impl.TaxRateService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/taxrates")
@Tag(name = "Taxrate", description = "API for managing taxrates")

public class TaxRateController {
@Autowired
private TaxRateService taxRateService;

 private final MappingProfile mapper = MappingProfile.INSTANCE;

    @GetMapping
    public CompletableFuture<ResponseEntity<GenericResponse<TaxRate>>> getTaxRate(
            @RequestParam int cityId,
            @RequestParam String time) {
        LocalTime localTime = LocalTime.parse(time);
        return taxRateService.getTaxRateByCityIdAndTimeAsync(cityId, localTime)
            .thenApply(taxRate -> ResponseEntity.ok(GenericResponse.success(taxRate)))
            .exceptionally(ex -> {
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
        });
    }

    @GetMapping("/city/{cityId}")
    public CompletableFuture<ResponseEntity<GenericResponse<List<TaxRate>>>>getTaxRatesByCityId(@PathVariable int cityId) {
        return taxRateService.getTaxRatesByCityIdAsync(cityId)
            .thenApply(taxRates -> ResponseEntity.ok(GenericResponse.success(taxRates)))
            .exceptionally(ex -> {
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
        });
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<GenericResponse<TaxRate>>> addTaxRate(@RequestBody TaxRateRequest request) {
        TaxRate map = mapper.taxRateRequestTaxRate(request);
        return taxRateService.addTaxRate(map)
         .thenApply(taxRate -> ResponseEntity.ok(GenericResponse.success(taxRate)))
         .exceptionally(ex -> {
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred: " + ex.getMessage()));
         });
    }
}
