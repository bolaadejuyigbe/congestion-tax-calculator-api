package com.example.congestion_tax_calculator_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.congestion_tax_calculator_api.model.CongestionEntry;

@Repository
public interface CongestionEntryRepository extends JpaRepository<CongestionEntry, Integer> {
    List<CongestionEntry> findByCityId(int cityId);
    List<CongestionEntry> findByVehicleRegistrationId(int vehicleRegistrationId);
}
