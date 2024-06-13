package com.example.congestion_tax_calculator_api.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "taxexemptions")
public class TaxExemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_exemption_Id")
    private int taxExemptionId;

    @Column(name = "CityID", nullable = false)
    private int cityId; 

    @Column(name = "exemption_date", nullable = false)
    private LocalDate exemptionDate;  

    @Column(name = "Description")
    private String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    // Getters and Setters
    public int getTaxExemptionId() {
        return taxExemptionId;
    }

    public void setTaxExemptionID(int taxExemptionId) {
        this.taxExemptionId = taxExemptionId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public LocalDate getExemptionDate() {
        return exemptionDate;
    }

    public void setExemptionDate(LocalDate exemptionDate) {
        this.exemptionDate = exemptionDate;
    }
}
