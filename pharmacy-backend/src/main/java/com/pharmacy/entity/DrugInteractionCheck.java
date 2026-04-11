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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrescriptionUpload getPrescriptionUpload() {
        return prescriptionUpload;
    }

    public void setPrescriptionUpload(PrescriptionUpload prescriptionUpload) {
        this.prescriptionUpload = prescriptionUpload;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public String getInteractingMedication() {
        return interactingMedication;
    }

    public void setInteractingMedication(String interactingMedication) {
        this.interactingMedication = interactingMedication;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}
