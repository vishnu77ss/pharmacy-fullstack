package com.pharmacy.service;

import com.pharmacy.dto.DispensingDTO;
import com.pharmacy.entity.*;
import com.pharmacy.enums.PrescriptionStatus;
import com.pharmacy.enums.Severity;
import com.pharmacy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispensingService {

    private final PrescriptionUploadRepository prescriptionRepo;
    private final UserRepository userRepository;
    private final DispensingRecordRepository dispensingRepo;
    private final RefillHistoryRepository refillHistoryRepository;
    private final DrugInteractionCheckRepository interactionRepo;

    public DispensingRecord dispense(DispensingDTO dto) {
        PrescriptionUpload p = prescriptionRepo.findById(dto.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        if (p.getStatus() != PrescriptionStatus.APPROVED) {
            throw new RuntimeException("Prescription must be APPROVED before dispensing");
        }

        // must dispense within 7 days of approval
        if (p.getApprovedAt() != null && p.getApprovedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Dispensing window expired (must dispense within 7 days of approval)");
        }

        // block if SEVERE interaction exists
        List<DrugInteractionCheck> interactions = interactionRepo.findByPrescriptionUploadId(p.getId());
        for (DrugInteractionCheck i : interactions) {
            if (i.getSeverity() == Severity.SEVERE) {
                throw new RuntimeException("Cannot dispense: SEVERE drug interaction detected with " + i.getInteractingMedication());
            }
            if (i.getSeverity() == Severity.CONTRAINDICATED && !p.isDoctorConfirmationReceived()) {
                throw new RuntimeException("Cannot dispense: CONTRAINDICATED interaction requires doctor confirmation");
            }
        }

        User pharmacist = userRepository.findById(dto.getPharmacistId())
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        int currentRefills = dispensingRepo.findByPrescriptionUploadId(p.getId()).size();

        DispensingRecord record = new DispensingRecord();
        record.setPrescriptionUpload(p);
        record.setDispensedBy(pharmacist);
        record.setRefillNumber(currentRefills + 1);
        record.setQuantityDispensed(dto.getQuantityDispensed());
        record.setDispensingDate(LocalDateTime.now());
        record.setExpiryDate(LocalDate.now().plusDays(90));
        dispensingRepo.save(record);

        p.setStatus(PrescriptionStatus.DISPENSED);
        prescriptionRepo.save(p);

        return record;
    }

    public DispensingRecord refill(DispensingDTO dto) {
        PrescriptionUpload p = prescriptionRepo.findById(dto.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        if (p.getRemainingRefills() <= 0) {
            throw new RuntimeException("No remaining refills allowed");
        }

        // check 30-day validity from upload
        if (p.getUploadedAt().plusDays(30).isBefore(LocalDateTime.now())) {
            p.setStatus(PrescriptionStatus.EXPIRED);
            prescriptionRepo.save(p);
            throw new RuntimeException("Prescription has expired");
        }

        User pharmacist = userRepository.findById(dto.getPharmacistId())
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        int refillNum = dispensingRepo.findByPrescriptionUploadId(p.getId()).size() + 1;

        DispensingRecord record = new DispensingRecord();
        record.setPrescriptionUpload(p);
        record.setDispensedBy(pharmacist);
        record.setRefillNumber(refillNum);
        record.setQuantityDispensed(dto.getQuantityDispensed());
        record.setDispensingDate(LocalDateTime.now());
        record.setExpiryDate(LocalDate.now().plusDays(90));
        dispensingRepo.save(record);

        p.setRemainingRefills(p.getRemainingRefills() - 1);
        prescriptionRepo.save(p);

        RefillHistory history = new RefillHistory();
        history.setPrescriptionUpload(p);
        history.setRefillNumber(refillNum);
        history.setDispensedBy(pharmacist);
        history.setRemainingRefills(p.getRemainingRefills());
        refillHistoryRepository.save(history);

        return record;
    }

    public List<DispensingRecord> getByPrescription(Long prescriptionId) {
        return dispensingRepo.findByPrescriptionUploadId(prescriptionId);
    }
}
