package com.pharmacy.entity;

import com.pharmacy.enums.PrescriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prescription_uploads")
public class PrescriptionUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull
    private User patient;

    @ManyToOne
    @JoinColumn(name = "medication_id")
    @NotNull
    private Medication medication;

    @NotBlank
    private String doctorName;

    @NotBlank
    private String doctorRegistrationNumber;

    private String dosage;
    private String frequency;
    private String duration;

    private int refillsRequested = 0;
    private int remainingRefills = 0;

    private String prescriptionImageUrl;

    @Column(updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    private LocalDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.UPLOADED;

    private boolean doctorConfirmationReceived = false;
}
