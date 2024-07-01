package com.example.congestion_tax_calculator_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicletypes")
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="vehicle_type_id")
    private int vehicleTypeId; 

    @Column(name ="Description")
    private String description;

    @Column(name ="is_exempt")
    private boolean isExempt;

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExempt() {
        return isExempt;
    }

    public void setExempt(boolean isExempt) {
        this.isExempt = isExempt;
    }

}
