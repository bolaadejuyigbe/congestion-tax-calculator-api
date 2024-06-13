package com.example.congestion_tax_calculator_api.payload.request;

import java.time.LocalDateTime;

public class CongestionEntryRequest {
    
private int cityId;
private int vehicleRegistrationId;
private LocalDateTime entryDate;
private String vehicleRegistrationNumber;

public String getVehicleRegistrationNumber() {
    return vehicleRegistrationNumber;
}
public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
    this.vehicleRegistrationNumber = vehicleRegistrationNumber;
}
public int getCityId() {
    return cityId;
}
public void setCityId(int cityId) {
    this.cityId = cityId;
}
public int getVehicleRegistrationId() {
    return vehicleRegistrationId;
}
public void setVehicleRegistrationId(int vehicleRegistrationId) {
    this.vehicleRegistrationId = vehicleRegistrationId;
}
public LocalDateTime getEntryDate() {
    return entryDate;
}
public void setEntryDate(LocalDateTime entryDate) {
    this.entryDate = entryDate;
}

}
