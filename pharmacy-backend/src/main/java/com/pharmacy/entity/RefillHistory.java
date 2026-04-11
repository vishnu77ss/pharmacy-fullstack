package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "refill_histories")
public class RefillHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private PrescriptionUpload prescriptionUpload;

    private int refillNumber;
    private LocalDateTime dispensedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "dispensed_by")
    private User dispensedBy;

    private int remainingRefills;

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

    public int getRefillNumber() {
        return refillNumber;
    }

    public void setRefillNumber(int refillNumber) {
        this.refillNumber = refillNumber;
    }

    public LocalDateTime getDispensedAt() {
        return dispensedAt;
    }

    public void setDispensedAt(LocalDateTime dispensedAt) {
        this.dispensedAt = dispensedAt;
    }

    public User getDispensedBy() {
        return dispensedBy;
    }

    public void setDispensedBy(User dispensedBy) {
        this.dispensedBy = dispensedBy;
    }

    public int getRemainingRefills() {
        return remainingRefills;
    }

    public void setRemainingRefills(int remainingRefills) {
        this.remainingRefills = remainingRefills;
    }
}
