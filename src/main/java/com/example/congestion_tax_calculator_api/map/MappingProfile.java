package com.example.congestion_tax_calculator_api.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.congestion_tax_calculator_api.model.CongestionEntry;
import com.example.congestion_tax_calculator_api.model.TaxExemption;
import com.example.congestion_tax_calculator_api.payload.request.CongestionEntryRequest;
import com.example.congestion_tax_calculator_api.payload.request.TaxExemptionRequest;

@Mapper
public interface MappingProfile {

    MappingProfile INSTANCE = Mappers.getMapper(MappingProfile.class);

    @Mapping(target = "entryId", ignore = true)
    CongestionEntry congestionEntryRequestToCongestionEntry(CongestionEntryRequest request);

    @Mapping(target = "taxExemptionID", ignore = true)
    TaxExemption taxExemptionRequesTaxExemption(TaxExemptionRequest request);
}
