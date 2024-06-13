package com.example.congestion_tax_calculator_api.repository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.example.congestion_tax_calculator_api.model.City;

@Repository
public interface CityRepository extends JpaRepository <City, Integer>{
    @Async
    CompletableFuture<List<City>> findAllBy();
    @Async
    CompletableFuture<Optional<City>> findById(int id);
}
