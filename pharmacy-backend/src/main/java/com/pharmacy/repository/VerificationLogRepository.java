package com.pharmacy.repository;

import com.pharmacy.entity.VerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {
    List<VerificationLog> findByPrescriptionUploadId(Long prescriptionId);
}
