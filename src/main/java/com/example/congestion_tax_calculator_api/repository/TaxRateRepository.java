package com.example.congestion_tax_calculator_api.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.example.congestion_tax_calculator_api.model.TaxRate;

public interface TaxRateRepository extends JpaRepository<TaxRate, Integer> {
    List<TaxRate> findByCityId(int cityId);
    @Async
      @Query(value = "SELECT * FROM taxrates WHERE CityID = :cityId " +
                   "AND ((end_time > start_time AND :time BETWEEN start_time AND end_time) " +
                   "OR (end_time <= start_time AND (:time >= start_time OR :time <= end_time))) " +
                   "LIMIT 1", nativeQuery = true)
         CompletableFuture<TaxRate> findTaxRate(@Param("cityId") int cityId, @Param("time") LocalTime time);
         Optional<TaxRate> findByCityIdAndStartTimeAndEndTime(int cityId, LocalTime startTime, LocalTime endTime);
}
