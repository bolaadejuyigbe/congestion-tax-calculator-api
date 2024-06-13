package com.example.congestion_tax_calculator_api.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.congestion_tax_calculator_api.model.City;
import com.example.congestion_tax_calculator_api.repository.CityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CityService {

@Autowired
private CityRepository cityRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    private static final Duration CACHE_TIME = Duration.ofMinutes(5);
@Async
public CompletableFuture<List<City>> getAllCities() {
        String cacheKey = "cities_all";
        List<City> cities;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        try {
            String cachedCities = valueOperations.get(cacheKey);
            if (cachedCities != null) {
                cities = objectMapper.readValue(cachedCities, objectMapper.getTypeFactory().constructCollectionType(List.class, City.class));
            } else {
                cities = cityRepository.findAllBy().get();
                String serializedCities = objectMapper.writeValueAsString(cities);
                valueOperations.set(cacheKey, serializedCities, CACHE_TIME);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON", e);
            throw new RuntimeException(e);
        } catch (Exception ex) {
            logger.error("Error retrieving all cities", ex);
            throw new RuntimeException(ex);
        }

        return CompletableFuture.completedFuture(cities);
    }

    public CompletableFuture<Optional<City>> getCityById(int cityId) {
        String cacheKey = "city_" + cityId;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        try {
            String cachedCity = valueOperations.get(cacheKey);
            if (cachedCity != null) {
               City city = objectMapper.readValue(cachedCity, City.class);
                return CompletableFuture.completedFuture(Optional.of(city));
            } else {
                Optional<City> cityOptional = cityRepository.findById(cityId).get();
                if (cityOptional.isPresent()) {
                    City city = cityOptional.get();
                    String serializedCity = objectMapper.writeValueAsString(city);
                    valueOperations.set(cacheKey, serializedCity, CACHE_TIME);
                    return CompletableFuture.completedFuture(cityOptional);
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON", e);
            throw new RuntimeException(e);
        } catch (Exception ex) {
            logger.error("Error retrieving city with ID " + cityId, ex);
            throw new RuntimeException(ex);
        }

        return CompletableFuture.completedFuture(Optional.empty());
    }
}
