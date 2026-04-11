package com.pharmacy.repository;

import com.pharmacy.entity.PrescriptionUpload;
import com.pharmacy.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PrescriptionUploadRepository extends JpaRepository<PrescriptionUpload, Long> {
    List<PrescriptionUpload> findByPatientId(Long patientId);

    @Query("SELECT p FROM PrescriptionUpload p JOIN p.medication m WHERE m.isControlledSubstance = true AND p.status = :status")
    List<PrescriptionUpload> findControlledSubstanceByStatus(PrescriptionStatus status);

    @Query("SELECT COUNT(p), p.status FROM PrescriptionUpload p GROUP BY p.status")
    List<Object[]> countByStatus();
}
