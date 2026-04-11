package com.pharmacy.repository;

import com.pharmacy.entity.ControlledSubstanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ControlledSubstanceLogRepository extends JpaRepository<ControlledSubstanceLog, Long> {
    List<ControlledSubstanceLog> findByPendingApprovalTrue();
    List<ControlledSubstanceLog> findByPrescriptionUploadPatientId(Long patientId);
}
