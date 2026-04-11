package com.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DispensingDTO {
    @NotNull
    private Long prescriptionId;

    @NotNull
    private Long pharmacistId;

    private int quantityDispensed;

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getPharmacistId() {
        return pharmacistId;
    }

    public void setPharmacistId(Long pharmacistId) {
        this.pharmacistId = pharmacistId;
    }

    public int getQuantityDispensed() {
        return quantityDispensed;
    }

    public void setQuantityDispensed(int quantityDispensed) {
        this.quantityDispensed = quantityDispensed;
    }
}
