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
}
