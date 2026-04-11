package com.pharmacy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionDTO {
    @NotNull
    private Long patientId;

    @NotNull
    private Long medicationId;

    @NotBlank
    private String doctorName;

    @NotBlank
    private String doctorRegistrationNumber;

    private String dosage;
    private String frequency;
    private String duration;
    private int refillsRequested;
    private String prescriptionImageUrl;
}
