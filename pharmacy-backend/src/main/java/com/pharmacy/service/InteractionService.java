package com.pharmacy.service;

import com.pharmacy.entity.DrugInteractionCheck;
import com.pharmacy.entity.PrescriptionUpload;
import com.pharmacy.entity.User;
import com.pharmacy.enums.Severity;
import com.pharmacy.repository.DrugInteractionCheckRepository;
import com.pharmacy.repository.PrescriptionUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final DrugInteractionCheckRepository interactionRepo;
    private final PrescriptionUploadRepository prescriptionRepo;

    public List<DrugInteractionCheck> checkForPrescription(Long prescriptionId) {
        // In real system, this would call an external drug interaction API
        // Here we return existing checks
        return interactionRepo.findByPrescriptionUploadId(prescriptionId);
    }

    public List<DrugInteractionCheck> getByPatient(Long patientId) {
        return interactionRepo.findByPatientId(patientId);
    }

    public DrugInteractionCheck addInteraction(Long prescriptionId, String interactingMed, Severity severity, String recommendation) {
        PrescriptionUpload p = prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        DrugInteractionCheck check = new DrugInteractionCheck();
        check.setPrescriptionUpload(p);
        check.setPatient(p.getPatient());
        check.setInteractingMedication(interactingMed);
        check.setSeverity(severity);
        check.setRecommendation(recommendation);
        return interactionRepo.save(check);
    }
}
