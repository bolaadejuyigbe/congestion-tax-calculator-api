package com.example.congestion_tax_calculator_api.service.Impl;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.congestion_tax_calculator_api.model.TaxExemption;
import com.example.congestion_tax_calculator_api.repository.TaxExemptionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaxExemptionService {
 @Autowired
 private TaxExemptionRepository taxExemptionRepository;

 @Autowired
 private RedisTemplate<String, String> redisTemplate;

 @Autowired
 private ObjectMapper objectMapper;

 private static final Logger logger = LoggerFactory.getLogger(TaxExemptionService.class);
 private static final Duration CACHE_TIME = Duration.ofMinutes(5);

    public TaxExemption addTaxExemption(TaxExemption taxExemption) {
        try {
            TaxExemption existingTaxExemption = taxExemptionRepository
                .findByCityIdAndExemptionDate(taxExemption.getCityId(), taxExemption.getExemptionDate());

            if (existingTaxExemption != null) {
                logger.warn("Tax exemption already exists for this city");
                throw new IllegalArgumentException(
                    "Tax exemption with this date " + taxExemption.getExemptionDate() + " is already registered for this city.");
            }

            taxExemptionRepository.save(taxExemption);
            String cacheKey = "taxExemption_" + taxExemption.getCityId();
            redisTemplate.delete(cacheKey);

            List<TaxExemption> updatedExemptions = taxExemptionRepository.findByCityId(taxExemption.getCityId());
            if (!updatedExemptions.isEmpty()) {
                String serializedExemptions = objectMapper.writeValueAsString(updatedExemptions);
                redisTemplate.opsForValue().set(cacheKey, serializedExemptions, CACHE_TIME);
            }

            return taxExemption;
        } catch (Exception ex) {
            logger.error("Error adding tax exemption for city ID {}", taxExemption.getCityId(), ex);
            throw new RuntimeException(ex);
        }
    }

    public List<TaxExemption> getExemptionsByCityId(int cityId) {
        String cacheKey = "taxExemption_" + cityId;
        List<TaxExemption> taxExemptions;

        try {
            String cachedTaxExemptions = redisTemplate.opsForValue().get(cacheKey);
            if (cachedTaxExemptions != null) {
                taxExemptions = objectMapper.readValue(cachedTaxExemptions, new TypeReference<List<TaxExemption>>() {});
            } else {
                taxExemptions = taxExemptionRepository.findByCityId(cityId);
                if (taxExemptions != null) {
                    String serializedTaxExemptions = objectMapper.writeValueAsString(taxExemptions);
                    redisTemplate.opsForValue().set(cacheKey, serializedTaxExemptions, CACHE_TIME);
                }
            }
        } catch (Exception ex) {
            logger.error("Error retrieving tax exemptions with ID {}", cityId, ex);
            throw new RuntimeException(ex);
        }

        return taxExemptions;
    }

   public boolean isDateExempt(int cityId, LocalDate date) {
    try {
        return taxExemptionRepository.existsByCityIdAndExemptionDate(cityId, date);
    } catch (Exception ex) {
        logger.error("Failed to check tax exemption for city ID {} on date {}", cityId, date, ex);
        throw new RuntimeException(ex);
    }
}
}
