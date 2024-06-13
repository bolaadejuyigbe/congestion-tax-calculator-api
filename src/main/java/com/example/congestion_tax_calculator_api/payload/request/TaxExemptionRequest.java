package com.example.congestion_tax_calculator_api.payload.request;

import java.time.LocalDate;

public class TaxExemptionRequest {
    private int cityId;
    private LocalDate ExemptionDate;
    private String Description;
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public LocalDate getExemptionDate() {
        return ExemptionDate;
    }
    public void setExemptionDate(LocalDate exemptionDate) {
        ExemptionDate = exemptionDate;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
}
