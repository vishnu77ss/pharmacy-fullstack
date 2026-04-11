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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorRegistrationNumber() {
        return doctorRegistrationNumber;
    }

    public void setDoctorRegistrationNumber(String doctorRegistrationNumber) {
        this.doctorRegistrationNumber = doctorRegistrationNumber;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getRefillsRequested() {
        return refillsRequested;
    }

    public void setRefillsRequested(int refillsRequested) {
        this.refillsRequested = refillsRequested;
    }

    public int getRemainingRefills() {
        return remainingRefills;
    }

    public void setRemainingRefills(int remainingRefills) {
        this.remainingRefills = remainingRefills;
    }

    public String getPrescriptionImageUrl() {
        return prescriptionImageUrl;
    }

    public void setPrescriptionImageUrl(String prescriptionImageUrl) {
        this.prescriptionImageUrl = prescriptionImageUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public boolean isDoctorConfirmationReceived() {
        return doctorConfirmationReceived;
    }

    public void setDoctorConfirmationReceived(boolean doctorConfirmationReceived) {
        this.doctorConfirmationReceived = doctorConfirmationReceived;
    }
}
