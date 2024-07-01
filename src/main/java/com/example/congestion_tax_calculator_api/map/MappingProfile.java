package com.example.congestion_tax_calculator_api.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.congestion_tax_calculator_api.model.CongestionEntry;
import com.example.congestion_tax_calculator_api.model.TaxExemption;
import com.example.congestion_tax_calculator_api.model.TaxRate;
import com.example.congestion_tax_calculator_api.payload.request.CongestionEntryRequest;
import com.example.congestion_tax_calculator_api.payload.request.TaxExemptionRequest;
import com.example.congestion_tax_calculator_api.payload.request.TaxRateRequest;

@Mapper
public interface MappingProfile {

    MappingProfile INSTANCE = Mappers.getMapper(MappingProfile.class);

    @Mapping(target = "entryId", ignore = true)
    CongestionEntry congestionEntryRequestToCongestionEntry(CongestionEntryRequest request);

    @Mapping(target = "taxExemptionID", ignore = true)
    TaxExemption taxExemptionRequestTaxExemption(TaxExemptionRequest request);

    @Mapping(target = "taxRateId", ignore = true)
    TaxRate taxRateRequestTaxRate(TaxRateRequest request);
}
