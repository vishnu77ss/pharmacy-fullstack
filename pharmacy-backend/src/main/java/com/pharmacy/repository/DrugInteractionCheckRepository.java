package com.pharmacy.repository;

import com.pharmacy.entity.DrugInteractionCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DrugInteractionCheckRepository extends JpaRepository<DrugInteractionCheck, Long> {
    List<DrugInteractionCheck> findByPrescriptionUploadId(Long prescriptionId);
    List<DrugInteractionCheck> findByPatientId(Long patientId);
}
