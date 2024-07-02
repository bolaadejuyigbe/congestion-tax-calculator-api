package com.example.congestion_tax_calculator_api.map;

import com.example.congestion_tax_calculator_api.model.City;
import com.example.congestion_tax_calculator_api.model.CongestionEntry;
import com.example.congestion_tax_calculator_api.model.TaxExemption;
import com.example.congestion_tax_calculator_api.model.TaxRate;
import com.example.congestion_tax_calculator_api.payload.request.CongestionEntryRequest;
import com.example.congestion_tax_calculator_api.payload.request.TaxExemptionRequest;
import com.example.congestion_tax_calculator_api.payload.request.TaxRateRequest;
import com.example.congestion_tax_calculator_api.payload.response.CityResponse;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-02T15:50:38+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240620-1855, environment: Java 17.0.11 (Eclipse Adoptium)"
)
public class MappingProfileImpl implements MappingProfile {

    @Override
    public CongestionEntry congestionEntryRequestToCongestionEntry(CongestionEntryRequest request) {
        if ( request == null ) {
            return null;
        }

        CongestionEntry congestionEntry = new CongestionEntry();

        congestionEntry.setVehicleRegistrationNumber( request.getVehicleRegistrationNumber() );
        congestionEntry.setCityId( request.getCityId() );
        congestionEntry.setVehicleRegistrationId( request.getVehicleRegistrationId() );
        congestionEntry.setEntryDate( request.getEntryDate() );

        return congestionEntry;
    }

    @Override
    public TaxExemption taxExemptionRequestTaxExemption(TaxExemptionRequest request) {
        if ( request == null ) {
            return null;
        }

        TaxExemption taxExemption = new TaxExemption();

        taxExemption.setDescription( request.getDescription() );
        taxExemption.setCityId( request.getCityId() );
        taxExemption.setExemptionDate( request.getExemptionDate() );

        return taxExemption;
    }

    @Override
    public TaxRate taxRateRequestTaxRate(TaxRateRequest request) {
        if ( request == null ) {
            return null;
        }

        TaxRate taxRate = new TaxRate();

        taxRate.setCityId( request.getCityId() );
        taxRate.setStartTime( request.getStartTime() );
        taxRate.setEndTime( request.getEndTime() );
        taxRate.setAmount( request.getAmount() );

        return taxRate;
    }

    @Override
    public CityResponse mapCitytoCityResponse(City city) {
        if ( city == null ) {
            return null;
        }

        CityResponse cityResponse = new CityResponse();

        cityResponse.setName( city.getName() );
        cityResponse.setCountry( city.getCountry() );
        cityResponse.setActive( city.isActive() );

        return cityResponse;
    }
}
