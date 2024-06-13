package com.example.congestion_tax_calculator_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.congestion_tax_calculator_api.map.MappingProfile;
import com.example.congestion_tax_calculator_api.model.CongestionEntry;
import com.example.congestion_tax_calculator_api.payload.request.CongestionEntryRequest;
import com.example.congestion_tax_calculator_api.service.CongestionEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/congestionentries")
@Tag(name = "CongestionEntry", description = "API for managing congestion entries")

public class CongestionEntryController {
 @Autowired
 private CongestionEntryService congestionEntryService;
  
 private final MappingProfile mapper = MappingProfile.INSTANCE;
    @GetMapping("/{cityId}")
    @Operation(summary = "Get congestionEntry by cityId")
    public List<CongestionEntry> getCongestionEntryByCityId(@PathVariable int cityId) {
        return congestionEntryService.getEntriesByCityId(cityId);
    }

    @GetMapping("/{VehicleRegistrationId}")
    @Operation(summary = "Get congestionEntry by vehicleregistrationid")
    public List<CongestionEntry> getCongestionEntryByVehicleRegistrationId(@PathVariable int vehicleRegistrationId) {
        return congestionEntryService.getEntriesByVehicleRegistrationId(vehicleRegistrationId);
    }

    @PostMapping
    public CongestionEntry createCongestionEntry(@RequestBody CongestionEntryRequest request) {
        CongestionEntry congestionEntry = mapper.congestionEntryRequestToCongestionEntry(request);   
        return congestionEntryService.saveCongestionEntry(congestionEntry);
    }

}
