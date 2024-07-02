package com.example.congestion_tax_calculator_api.CityTest;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.congestion_tax_calculator_api.controller.CityController;
import com.example.congestion_tax_calculator_api.payload.response.CityResponse;
import com.example.congestion_tax_calculator_api.service.Impl.CityService;


@WebMvcTest(controllers =  CityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CityControllerTest {

    @MockBean
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
    }

   @Test
    public void testGetAllCities() throws Exception {
        CityResponse city1 = new CityResponse();
        city1.setName("Stockholm");
        city1.setCountry("Sweden");
        city1.setActive(true);

        CityResponse city2 = new CityResponse();
        city2.setName("Gothenburg");
        city2.setCountry("Sweden");
        city2.setActive(true);

        when(cityService.getAllCities()).thenReturn(CompletableFuture.completedFuture(Arrays.asList(city1, city2)));

        MvcResult result = mockMvc.perform(get("/api/cities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("Response: " + jsonResponse);

        mockMvc.perform(get("/api/cities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("SUCCESS!"))
                .andExpect(jsonPath("$.data[0].cityID").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Stockholm"))
                .andExpect(jsonPath("$.data[0].country").value("Sweden"))
                .andExpect(jsonPath("$.data[0].active").value(true))
                .andExpect(jsonPath("$.data[1].cityID").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Gothenburg"))
                .andExpect(jsonPath("$.data[1].country").value("Sweden"))
                .andExpect(jsonPath("$.data[1].active").value(true));
    }

    @Test
    public void testGetCityById() throws Exception {
        CityResponse city = new CityResponse();
        city.setName("Stockholm");
        city.setCountry("Sweden");
        city.setActive(true);

        when(cityService.getCityById(1)).thenReturn(CompletableFuture.completedFuture(Optional.of(city)));

        MvcResult result = mockMvc.perform(get("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("Response: " + jsonResponse);

        mockMvc.perform(get("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("SUCCESS!"))
                .andExpect(jsonPath("$.data.cityID").value(1))
                .andExpect(jsonPath("$.data.name").value("Stockholm"))
                .andExpect(jsonPath("$.data.country").value("Sweden"))
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    public void testGetCityById_NotFound() throws Exception {
        when(cityService.getCityById(1)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        MvcResult result = mockMvc.perform(get("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("Response: " + jsonResponse);

        mockMvc.perform(get("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("City not found"));
    }
}
