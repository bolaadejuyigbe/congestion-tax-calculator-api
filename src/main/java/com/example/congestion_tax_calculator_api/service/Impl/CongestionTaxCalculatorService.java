package com.example.congestion_tax_calculator_api.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.congestion_tax_calculator_api.service.ICongestionCalculatorService;

@Service
public class CongestionTaxCalculatorService implements ICongestionCalculatorService {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private TaxExemptionService taxExemptionService;
    @Autowired
    private CityService cityService;
    @Autowired
    private TaxRateService taxRateService;

    private static final Logger logger = LoggerFactory.getLogger(CongestionTaxCalculatorService.class);
   
    @Async
    public CompletableFuture<BigDecimal> calculateTaxAsync(int cityId, String vehicleRegId, int vehicleTypeId, List<LocalDateTime> entryDates) {
        return cityService.getCityById(cityId)
            .thenCompose(optionalCity -> {
                if (!optionalCity.isPresent() || !optionalCity.get().isActive()) {
                    return CompletableFuture.completedFuture(BigDecimal.ZERO);
                }
                return vehicleService.getVehicleTypeById(vehicleTypeId)
                    .thenCompose(optionalVehicleType -> {
                        if (!optionalVehicleType.isPresent() || optionalVehicleType.get().isExempt()) {
                            return CompletableFuture.completedFuture(BigDecimal.ZERO);
                        }
                        return processTaxComputation(cityId, vehicleRegId, vehicleTypeId, entryDates);
                    });
            })
            .exceptionally(ex -> {
                logger.error("Failed to calculate congestion tax for vehicle ID {}", vehicleRegId, ex);
                throw new IllegalStateException("Error calculating congestion tax", ex);
            });
        }
    
    private CompletableFuture<BigDecimal> processTaxComputation(int cityId, String vehicleRegId, int vehicleTypeId, List<LocalDateTime> entryDates) {
        return vehicleService.getVehicleTypeById(vehicleTypeId)
            .thenComposeAsync(vehicleType -> {
                if (!vehicleType.isPresent()) {
                    return CompletableFuture.completedFuture(BigDecimal.ZERO);
                }
                return calculateDailyCharges(cityId, entryDates);
            });
    }
    private CompletableFuture<BigDecimal> calculateDailyCharges(int cityId, List<LocalDateTime> entryDateTimes) {
        // Grouping LocalDateTime entries by LocalDate and collecting associated LocalTime
        Map<LocalDate, List<LocalTime>> entriesGroupedByDate = entryDateTimes.stream()
            .collect(Collectors.groupingBy(
                LocalDateTime::toLocalDate,
                Collectors.mapping(LocalDateTime::toLocalTime, Collectors.toList())
            ));

        BigDecimal dailyMaxTax = BigDecimal.valueOf(60);
        CompletableFuture<BigDecimal> totalTax = CompletableFuture.completedFuture(BigDecimal.ZERO);

        for (Map.Entry<LocalDate, List<LocalTime>> entry : entriesGroupedByDate.entrySet()) {
            CompletableFuture<BigDecimal> dailyTotalTaxFuture = calculateTaxForDay(cityId, entry.getKey(), entry.getValue(), dailyMaxTax);
            totalTax = totalTax.thenCombine(dailyTotalTaxFuture, BigDecimal::add);
        }

        return totalTax;
    }
    private CompletableFuture<BigDecimal> calculateTaxForDay(int cityId, LocalDate date, List<LocalTime> times, BigDecimal  dailyMaxTax) {
        BigDecimal dailyTotalTax = BigDecimal.ZERO;
        LocalTime lastChargeTime = LocalTime.MIN;

        List<LocalTime> sortedTimes = times.stream().sorted().collect(Collectors.toList());

        for (LocalTime time : sortedTimes) {
            if (taxExemptionService.isDateExempt(cityId, date)) {
                continue;
            }

            if (!lastChargeTime.equals(LocalTime.MIN) && time.minusHours(1).isBefore(lastChargeTime)) {
                continue;
            }

            BigDecimal applicableRate = taxRateService.getTaxRateByCityIdAndTimeAsync(cityId, time).join().getAmount();
            if (applicableRate != null) {
                dailyTotalTax = dailyTotalTax.add(applicableRate);
                lastChargeTime = time;

                if (dailyTotalTax.compareTo(dailyMaxTax) >= 0) {
                    return CompletableFuture.completedFuture(dailyMaxTax);
                }
            }
        }
        return CompletableFuture.completedFuture(dailyTotalTax); 
    }
}
