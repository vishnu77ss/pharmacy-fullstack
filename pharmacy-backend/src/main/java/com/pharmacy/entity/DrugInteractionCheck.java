package com.pharmacy.entity;

import com.pharmacy.enums.Severity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "drug_interaction_checks")
public class DrugInteractionCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private PrescriptionUpload prescriptionUpload;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    private String interactingMedication;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    private LocalDateTime checkedAt = LocalDateTime.now();
}
