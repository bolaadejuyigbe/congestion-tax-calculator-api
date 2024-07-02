package com.example.congestion_tax_calculator_api.service.Impl;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.congestion_tax_calculator_api.map.MappingProfile;
import com.example.congestion_tax_calculator_api.model.City;
import com.example.congestion_tax_calculator_api.payload.response.CityResponse;
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

private final MappingProfile mapper = MappingProfile.INSTANCE;

private static final Logger logger = LoggerFactory.getLogger(CityService.class);

private static final Duration CACHE_TIME = Duration.ofMinutes(5);
    @Async
    public CompletableFuture<List<CityResponse>> getAllCities() {
    return CompletableFuture.supplyAsync(() -> {
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
            List<CityResponse> cityResponses = cities.stream().map(mapper::mapCitytoCityResponse).collect(Collectors.toList());
            logger.info("Returning cities: {}", cityResponses);
            return cityResponses;
      });
    }

    public CompletableFuture<Optional<CityResponse>> getCityById(int cityId) {
        return CompletableFuture.supplyAsync(() -> {
            String cacheKey = "city_" + cityId;
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

            try {
                String cachedCity = valueOperations.get(cacheKey);
                if (cachedCity != null) {
                    City city = objectMapper.readValue(cachedCity, City.class);
                    return Optional.of(mapper.mapCitytoCityResponse(city));
                }
                Optional<City> cityOptional = cityRepository.findById(cityId).get();
                cityOptional.ifPresent(city -> {
                    try {
                        String serializedCity = objectMapper.writeValueAsString(city);
                        valueOperations.set(cacheKey, serializedCity, CACHE_TIME);
                    } catch (JsonProcessingException e) {
                        logger.error("Error serializing city data", e);
                    }
                });
                return cityOptional.map(mapper::mapCitytoCityResponse);
            } catch (JsonProcessingException e) {
                logger.error("Error processing JSON", e);
                throw new RuntimeException("Error processing JSON", e);
            } catch (Exception ex) {
                logger.error("Error retrieving city with ID {}", cityId, ex);
                throw new RuntimeException("Error retrieving city", ex);
            }
        });
    }
}
