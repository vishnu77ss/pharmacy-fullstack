package com.pharmacy.dto;

import com.pharmacy.enums.Schedule;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MedicationDTO {
    @NotBlank
    private String medicationName;

    private String genericName;
    private String drugClass;
    private boolean isControlledSubstance;
    private Schedule controlledSchedule;
    private int maxRefillsAllowed;
    private boolean requiresPrescription = true;
    private String contraindications;

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(String drugClass) {
        this.drugClass = drugClass;
    }

    public boolean isControlledSubstance() {
        return isControlledSubstance;
    }

    public void setControlledSubstance(boolean controlledSubstance) {
        isControlledSubstance = controlledSubstance;
    }

    public Schedule getControlledSchedule() {
        return controlledSchedule;
    }

    public void setControlledSchedule(Schedule controlledSchedule) {
        this.controlledSchedule = controlledSchedule;
    }

    public int getMaxRefillsAllowed() {
        return maxRefillsAllowed;
    }

    public void setMaxRefillsAllowed(int maxRefillsAllowed) {
        this.maxRefillsAllowed = maxRefillsAllowed;
    }

    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }

    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }
}
