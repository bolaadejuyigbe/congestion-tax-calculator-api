package com.example.congestion_tax_calculator_api.service.Impl;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.congestion_tax_calculator_api.model.CongestionEntry;
import com.example.congestion_tax_calculator_api.repository.CityRepository;
import com.example.congestion_tax_calculator_api.repository.CongestionEntryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CongestionEntryService {
  @Autowired
    private CongestionEntryRepository congestionEntryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CongestionEntryService.class);
    private static final Duration CACHE_TIME = Duration.ofMinutes(5);
    @Cacheable(value = "congestionEntry", key = "#cityID")
    public List<CongestionEntry> getEntriesByCityId(int cityId) {
        return congestionEntryRepository.findByCityId(cityId);
    }
    @Cacheable(value = "congestionEntry", key = "#vehicleRegistrationID")
    public List<CongestionEntry> getEntriesByVehicleRegistrationId(int vehicleRegistrationId) {
        return congestionEntryRepository.findByVehicleRegistrationId(vehicleRegistrationId);
    }
    public CongestionEntry saveCongestionEntry(CongestionEntry congestionEntry) {
        String cacheKey = "congestion_entry_" + congestionEntry.getCityId();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        try {
            boolean cityExists = cityRepository.existsById(congestionEntry.getCityId());
            if (!cityExists) {
                logger.warn("Invalid cityId: {}", congestionEntry.getCityId());
                throw new IllegalArgumentException("Invalid cityId: " + congestionEntry.getCityId());
            }
            CongestionEntry savedEntry = congestionEntryRepository.save(congestionEntry);
            try {
                String cachedData = valueOperations.get(cacheKey);
                if (cachedData != null) {
                    valueOperations.set(cacheKey, objectMapper.writeValueAsString(savedEntry), CACHE_TIME);
                }
            } catch (JsonProcessingException e) {
                logger.error("Error processing JSON", e);
                throw new RuntimeException("Error processing JSON", e);
            }

            return savedEntry;
        } catch (Exception ex) {
            logger.error("Error saving congestion entry", ex);
            throw new RuntimeException("Error saving congestion entry", ex);
        }
    }
}
