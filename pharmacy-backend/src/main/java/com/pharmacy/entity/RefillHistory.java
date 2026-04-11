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
}
