package com.example.congestion_tax_calculator_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VehicleTypes")
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="vehicle_type_id")
    private int VehicleTypeID; 

    @Column(name ="Description")
    private String Description;

    @Column(name ="IsExempt")
    private boolean IsExempt;

    public int getVehicleTypeID() {
        return VehicleTypeID;
    }

    public void setVehicleTypeID(int vehicleTypeID) {
        VehicleTypeID = vehicleTypeID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isIsExempt() {
        return IsExempt;
    }

    public void setIsExempt(boolean isExempt) {
        IsExempt = isExempt;
    }

}
