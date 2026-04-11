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
}
