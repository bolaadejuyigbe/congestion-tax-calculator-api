package com.example.congestion_tax_calculator_api.map;

import com.example.congestion_tax_calculator_api.model.CongestionEntry;
import com.example.congestion_tax_calculator_api.model.TaxExemption;
import com.example.congestion_tax_calculator_api.payload.request.CongestionEntryRequest;
import com.example.congestion_tax_calculator_api.payload.request.TaxExemptionRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-13T19:32:44+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240524-2033, environment: Java 17.0.11 (Eclipse Adoptium)"
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
    public TaxExemption taxExemptionRequesTaxExemption(TaxExemptionRequest request) {
        if ( request == null ) {
            return null;
        }

        TaxExemption taxExemption = new TaxExemption();

        taxExemption.setDescription( request.getDescription() );
        taxExemption.setCityId( request.getCityId() );
        taxExemption.setExemptionDate( request.getExemptionDate() );

        return taxExemption;
    }
}
