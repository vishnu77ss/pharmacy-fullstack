package com.pharmacy.entity;

import com.pharmacy.enums.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String medicationName;

    private String genericName;
    private String drugClass;

    private boolean isControlledSubstance = false;

    @Enumerated(EnumType.STRING)
    private Schedule controlledSchedule;

    private int maxRefillsAllowed = 0;
    private boolean requiresPrescription = true;

    @Column(columnDefinition = "TEXT")
    private String contraindications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
