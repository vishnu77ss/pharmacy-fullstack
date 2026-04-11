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
}
