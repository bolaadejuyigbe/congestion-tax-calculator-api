package com.example.congestion_tax_calculator_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "congestionentries")
public class CongestionEntry {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "entry_id", nullable = false)
private int entryId;
@Column(name = "CityID", nullable = false)
private int cityId;
@Column(name = "vehicle_registrationid", nullable = false)
private int vehicleRegistrationId;

@Column(name = "vehicle_registration_number", nullable = false)
private String vehicleRegistrationNumber;

public String getVehicleRegistrationNumber() {
    return vehicleRegistrationNumber;
}
public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
    this.vehicleRegistrationNumber = vehicleRegistrationNumber;
}

@Column(name = "entry_date_time", nullable = false)
private LocalDateTime entryDate;

public int getEntryId(){
    return entryId;
}
public void setEntryId(int entryId){
    this.entryId = entryId;
}
public int getCityId(){
    return cityId;
}
public void setCityId(int cityId){
 this.cityId = cityId;
}
public int getVehicleRegistrationId(){
    return vehicleRegistrationId;
} 
public void setVehicleRegistrationId(int vehicleRegistrationId){
    this.vehicleRegistrationId = vehicleRegistrationId;
}
public LocalDateTime getEntryDate() {
    return entryDate;
}

public void setEntryDate(LocalDateTime entryDate) {
    this.entryDate = entryDate;
}
}
