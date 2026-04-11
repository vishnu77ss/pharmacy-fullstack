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
}
