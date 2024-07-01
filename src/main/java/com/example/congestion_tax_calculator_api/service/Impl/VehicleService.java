package com.example.congestion_tax_calculator_api.service.Impl;

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

import com.example.congestion_tax_calculator_api.model.VehicleType;
import com.example.congestion_tax_calculator_api.repository.VehicleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VehicleService {
@Autowired
private VehicleRepository vehicleRepository;

 @Autowired
 private RedisTemplate<String, String> redisTemplate;

 @Autowired
 private ObjectMapper objectMapper;

 private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
 private static final Duration CACHE_TIME = Duration.ofMinutes(5);

   @Async 
   public CompletableFuture<List<VehicleType>> getAllVehicleTypes()
   {
    return CompletableFuture.supplyAsync(() -> {
    String cacheKey = "vehicleTypes_all";
    List<VehicleType> vehicleTypes;
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    try {
        String cachedVehicle = valueOperations.get(cacheKey);
        if(cachedVehicle != null){
            vehicleTypes = objectMapper.readValue(cachedVehicle, objectMapper.getTypeFactory().constructCollectionType(List.class, VehicleType.class));
        }else{
            vehicleTypes = vehicleRepository.findAllBy().get();
            String serializedVehicles = objectMapper.writeValueAsString(vehicleTypes);
            valueOperations.set(cacheKey, serializedVehicles, CACHE_TIME);
        }
    }catch(JsonProcessingException e) {
            logger.error("Error processing JSON", e);
            throw new RuntimeException(e);
    }catch (Exception e) {
        logger.error("Error retrieving all vehicles", e);
        throw new RuntimeException(e);
    }
      return vehicleTypes;
   });
   }

   public CompletableFuture <Optional<VehicleType>> getVehicleTypeById(int vehicleTypeId) 
   {
    return CompletableFuture.supplyAsync(() -> {
    String cacheKey = "vehicleTypes_" + vehicleTypeId;
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    try {
        String cachedVehicle = valueOperations.get(cacheKey);
        if(cachedVehicle != null){
             VehicleType vehicleType = objectMapper.readValue(cachedVehicle, VehicleType.class);
             return Optional.of(vehicleType);
        }else{
          Optional<VehicleType> vehicleTypeOptional = vehicleRepository.findByVehicleTypeId(vehicleTypeId).get();
           if (vehicleTypeOptional.isPresent()) {
                    VehicleType vehicleType = vehicleTypeOptional.get();
                    String serializedVehicleType = objectMapper.writeValueAsString(vehicleType);
                    valueOperations.set(cacheKey, serializedVehicleType, CACHE_TIME);
                    return vehicleTypeOptional;
            }
        }
    }catch (JsonProcessingException e) {
        logger.error("Error processing JSON", e);
        throw new RuntimeException(e);
    } catch (Exception e) {
        logger.error("Error retrieving vehicletype with ID " + vehicleTypeId, e);
        throw new RuntimeException(e);
    }
    return Optional.empty();
    });
   }

   public CompletableFuture<Boolean> isVehicleTypeExemptAsync(int vehicleTypeId) {
       return CompletableFuture.supplyAsync(() -> {
           try {
              Optional<VehicleType> vehicleType = vehicleRepository.findByVehicleTypeId(vehicleTypeId).get(); // Assuming context method to fetch by ID
               return vehicleType != null ? vehicleType.isPresent() : false;
           } catch (Exception ex) {
               logger.error("An error occurred: {}", ex.getMessage());
               throw new RuntimeException("Failed to check vehicle type exemption", ex);
           }
       });
   }
}
