package com.example.congestion_tax_calculator_api.payload.request;

import java.math.BigDecimal;
import java.time.LocalTime;


public class TaxRateRequest {

    private int cityId; 
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal amount;
    
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
