package com.example.congestion_tax_calculator_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.congestion_tax_calculator_api.model.TaxExemption;

@Repository
public interface TaxExemptionRepository extends JpaRepository<TaxExemption, Integer> {
    TaxExemption findByCityIdAndExemptionDate(int cityId, LocalDate exemptionDate);
    List<TaxExemption> findByCityId(int cityId);
    boolean existsByCityIdAndExemptionDate(int cityId, LocalDate exemptionDate); 
}
