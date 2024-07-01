package com.example.congestion_tax_calculator_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface ICongestionCalculatorService {

   @Async 
   CompletableFuture<BigDecimal> calculateTaxAsync(int cityId, String vehicleRegId, int vehicleTypeId, List<LocalDateTime> entryDates);
}

