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
}
