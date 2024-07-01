package com.example.congestion_tax_calculator_api.service.Impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.congestion_tax_calculator_api.model.TaxRate;
import com.example.congestion_tax_calculator_api.repository.TaxRateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaxRateService {

    @Autowired
    private TaxRateRepository taxRateRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    private static final Duration CACHE_TIME = Duration.ofMinutes(5);

    @Async
    public CompletableFuture<List<TaxRate>> getTaxRatesByCityIdAsync(int cityId) {
     return CompletableFuture.supplyAsync(() -> {
        String cacheKey = "taxRates_" + cityId;
        List<TaxRate> taxRates = null;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        try {
            String cachedTaxRates = valueOperations.get(cacheKey);
            if (cachedTaxRates != null) {
               taxRates = objectMapper.readValue(cachedTaxRates, objectMapper.getTypeFactory().constructCollectionType(  List.class, TaxRate.class));
            }else{
                taxRates = taxRateRepository.findByCityId(cityId);
                valueOperations.set(cacheKey, objectMapper.writeValueAsString(taxRates), CACHE_TIME);
            }
        } catch (Exception ex) {
            logger.error("Failed to retrieve tax rates by city", ex);
        }
        return taxRates != null ? taxRates : List.of();
     });
    }

    @Async
    public CompletableFuture<TaxRate> getTaxRateByCityIdAndTimeAsync(int cityId, LocalTime time) {
     return taxRateRepository.findTaxRate(
                    cityId, time)
            .exceptionally(ex -> {
                logger.error("Error retrieving Tax rate for city ID " + cityId, ex);
                throw new RuntimeException(ex);
            });
     }
     
    @Async
    public CompletableFuture<TaxRate> addTaxRate(TaxRate taxRate) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            String cacheKey = "taxRates_" + taxRate.getCityId(); 
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String cachedTaxRate = valueOperations.get(cacheKey);
            if (cachedTaxRate != null) {
                logger.warn("Tax rate already exists for this city and time range.");
                throw new Exception("Tax rate with timestamp from " + taxRate.getStartTime() + " to " + taxRate.getEndTime() + " is already registered for this city.");
            }
                TaxRate existingTaxRate = taxRateRepository.findByCityIdAndStartTimeAndEndTime(taxRate.getCityId(), taxRate.getStartTime(), taxRate.getEndTime()).orElse(null);
                if (existingTaxRate != null) {
                    logger.warn("Tax rate already exists for this city and time range.");
                    throw new Exception("Tax rate with timestamp from " + taxRate.getStartTime() + " to " + taxRate.getEndTime() + " is already registered for this city.");
                }

                TaxRate savedTaxRate = taxRateRepository.save(taxRate);

                String serializedTaxRate = objectMapper.writeValueAsString(savedTaxRate);
                valueOperations.set(cacheKey, serializedTaxRate, CACHE_TIME);

                String cityCacheKey = "taxRates_" + taxRate.getCityId();
                valueOperations.getOperations().delete(cityCacheKey);
                List<TaxRate> updatedRates = taxRateRepository.findByCityId(taxRate.getCityId());
                if (!updatedRates.isEmpty()) {
                    valueOperations.set(cityCacheKey, objectMapper.writeValueAsString(updatedRates), CACHE_TIME);
                }
                return savedTaxRate;
            } catch (JsonProcessingException e) {
                logger.error("Error processing JSON", e);
                throw new RuntimeException(e);
            } catch (Exception ex) {
                logger.error("Error adding tax rate", ex);
                throw new RuntimeException(ex);
            }
        });
    }   
}
