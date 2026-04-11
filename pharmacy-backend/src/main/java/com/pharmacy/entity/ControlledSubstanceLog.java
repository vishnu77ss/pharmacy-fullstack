package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "controlled_substance_logs")
public class ControlledSubstanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private PrescriptionUpload prescriptionUpload;

    @ManyToOne
    @JoinColumn(name = "approving_manager")
    private User approvingManager;

    private int quantityDispensed;
    private String prescribingDoctor;

    private LocalDateTime approvedAt;
    private boolean pendingApproval = true;

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

    public User getApprovingManager() {
        return approvingManager;
    }

    public void setApprovingManager(User approvingManager) {
        this.approvingManager = approvingManager;
    }

    public int getQuantityDispensed() {
        return quantityDispensed;
    }

    public void setQuantityDispensed(int quantityDispensed) {
        this.quantityDispensed = quantityDispensed;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public boolean isPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(boolean pendingApproval) {
        this.pendingApproval = pendingApproval;
    }
}
