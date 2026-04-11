package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dispensing_records")
public class DispensingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private PrescriptionUpload prescriptionUpload;

    @ManyToOne
    @JoinColumn(name = "dispensed_by")
    private User dispensedBy;

    private int refillNumber;
    private int quantityDispensed;

    private LocalDateTime dispensingDate = LocalDateTime.now();
    private LocalDate expiryDate;

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

    public User getDispensedBy() {
        return dispensedBy;
    }

    public void setDispensedBy(User dispensedBy) {
        this.dispensedBy = dispensedBy;
    }

    public int getRefillNumber() {
        return refillNumber;
    }

    public void setRefillNumber(int refillNumber) {
        this.refillNumber = refillNumber;
    }

    public int getQuantityDispensed() {
        return quantityDispensed;
    }

    public void setQuantityDispensed(int quantityDispensed) {
        this.quantityDispensed = quantityDispensed;
    }

    public LocalDateTime getDispensingDate() {
        return dispensingDate;
    }

    public void setDispensingDate(LocalDateTime dispensingDate) {
        this.dispensingDate = dispensingDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
