package com.example.congestion_tax_calculator_api.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import com.example.congestion_tax_calculator_api.model.TaxRate;

public interface TaxRateRepository extends JpaRepository<TaxRate, Integer> {
    List<TaxRate> findByCityId(int cityId);
    @Async
    CompletableFuture<TaxRate> findFirstByCityIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            int cityId, LocalTime time1, LocalTime time2, LocalTime time3, LocalTime time4);
            Optional<TaxRate> findByCityIdAndStartTimeAndEndTime(int cityId, LocalTime startTime, LocalTime endTime);
}
