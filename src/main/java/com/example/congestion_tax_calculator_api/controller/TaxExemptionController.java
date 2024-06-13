package com.example.congestion_tax_calculator_api.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.example.congestion_tax_calculator_api.model.TaxExemption;
import com.example.congestion_tax_calculator_api.payload.request.TaxExemptionRequest;
import com.example.congestion_tax_calculator_api.payload.response.GenericResponse;
import com.example.congestion_tax_calculator_api.service.TaxExemptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/taxexemptions")
@Tag(name = "TaxExemptions", description = "API for managing tax exemption Date")
public class TaxExemptionController {

    @Autowired
    private TaxExemptionService taxExemptionService;
    private final MappingProfile mapper = MappingProfile.INSTANCE;

    @PostMapping
    @Operation(summary = "Creates tax exemption date")
    public ResponseEntity<GenericResponse<TaxExemption>>addTaxExemption(@RequestBody TaxExemptionRequest request){
       try
       {
        TaxExemption taxExemption = mapper.taxExemptionRequesTaxExemption(request);
        TaxExemption addedTaxExemption = taxExemptionService.addTaxExemption(taxExemption);
        return ResponseEntity.ok(GenericResponse.success(addedTaxExemption));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(GenericResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred while adding the tax exemption."));
       }
    }
    
    @GetMapping("/{cityId}")
    @Operation(summary = "Get tax exemption date by cityId")
    public ResponseEntity<GenericResponse<List<TaxExemption>>> getExemptionsByCityId(@PathVariable int cityId) {
        try {
            List<TaxExemption> taxExemptions = taxExemptionService.getExemptionsByCityId(cityId);
            return ResponseEntity.ok(GenericResponse.success(taxExemptions));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred while retrieving tax exemptions."));
        }
    }

    @GetMapping("/isDateExempt")
    @Operation(summary = "check if a date is exempted from tax")
    public ResponseEntity<GenericResponse<Boolean>> isDateExempt(@RequestParam int cityId, @RequestParam String date) {
        try {
            boolean isExempt = taxExemptionService.isDateExempt(cityId, LocalDate.parse(date));
            return ResponseEntity.ok(GenericResponse.success(isExempt));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(GenericResponse.error("An error occurred while checking if the date is exempt."));
        }
    }
}
