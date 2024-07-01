package com.example.congestion_tax_calculator_api.payload.request;

import java.time.LocalDateTime;
import java.util.List;

public class CongestionTaxcalculationRequest {

    private int cityId;
    private String vehicleRegId;
    private int vehicleTypeId;
    private List<LocalDateTime> entryDates;
    
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public String getVehicleRegId() {
        return vehicleRegId;
    }
    public void setVehicleRegId(String vehicleRegId) {
        this.vehicleRegId = vehicleRegId;
    }
    public int getVehicleTypeId() {
        return vehicleTypeId;
    }
    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
    public List<LocalDateTime> getEntryDates() {
        return entryDates;
    }
    public void setEntryDates(List<LocalDateTime> entryDates) {
        this.entryDates = entryDates;
    }
}
