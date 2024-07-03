package com.example.congestion_tax_calculator_api.CityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.example.congestion_tax_calculator_api.model.City;
import com.example.congestion_tax_calculator_api.payload.response.CityResponse;
import com.example.congestion_tax_calculator_api.repository.CityRepository;
import com.example.congestion_tax_calculator_api.service.Impl.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testGetAllCities() throws Exception {
        City city1 = new City();
        city1.setCityID(1);
        city1.setName("Stockholm");
        city1.setCountry("Sweden");
        city1.setActive(true);

        City city2 = new City();
        city2.setCityID(2);
        city2.setName("Gothenburg");
        city2.setCountry("Sweden");
        city2.setActive(true);

        when(cityRepository.findAllBy()).thenReturn(CompletableFuture.completedFuture(Arrays.asList(city1, city2)));

        CompletableFuture<List<CityResponse>> future = cityService.getAllCities();
        List<CityResponse> cities = future.get();

        assertEquals(2, cities.size());
        assertEquals("Stockholm", cities.get(0).getName());
        assertEquals("Gothenburg", cities.get(1).getName());
    }

    @Test
    public void testGetCityById() throws Exception {
        City city = new City();
        city.setCityID(1);
        city.setName("Stockholm");
        city.setCountry("Sweden");
        city.setActive(true);

        when(cityRepository.findById(1)).thenReturn(CompletableFuture.completedFuture(Optional.of(city)));

        CompletableFuture<Optional<CityResponse>> future = cityService.getCityById(1);
        Optional<CityResponse> cityOptional = future.get();

        assertTrue(cityOptional.isPresent());
        assertEquals("Stockholm", cityOptional.get().getName());
    }
}

