package com.pharmacy.service;

import com.pharmacy.dto.PrescriptionDTO;
import com.pharmacy.dto.VerificationDTO;
import com.pharmacy.entity.*;
import com.pharmacy.enums.PrescriptionStatus;
import com.pharmacy.enums.Role;
import com.pharmacy.enums.Schedule;
import com.pharmacy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionUploadRepository prescriptionRepo;
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;
    private final VerificationLogRepository verificationLogRepository;
    private final ControlledSubstanceLogRepository controlledLogRepository;

    public PrescriptionUpload upload(PrescriptionDTO dto) {
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (patient.getRole() != Role.PATIENT) {
            throw new RuntimeException("Only patients can upload prescriptions");
        }

        Medication medication = medicationRepository.findById(dto.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        PrescriptionUpload p = new PrescriptionUpload();
        p.setPatient(patient);
        p.setMedication(medication);
        p.setDoctorName(dto.getDoctorName());
        p.setDoctorRegistrationNumber(dto.getDoctorRegistrationNumber());
        p.setDosage(dto.getDosage());
        p.setFrequency(dto.getFrequency());
        p.setDuration(dto.getDuration());
        p.setPrescriptionImageUrl(dto.getPrescriptionImageUrl());

        int requestedRefills = dto.getRefillsRequested();
        int maxAllowed = medication.getMaxRefillsAllowed();

        if (medication.isControlledSubstance() && medication.getControlledSchedule() == Schedule.II) {
            p.setRefillsRequested(0);
            p.setRemainingRefills(0);
        } else {
            int granted = Math.min(requestedRefills, maxAllowed);
            p.setRefillsRequested(granted);
            p.setRemainingRefills(granted);
        }

        p.setStatus(PrescriptionStatus.UPLOADED);

        PrescriptionUpload saved = prescriptionRepo.save(p);

        // if controlled substance, create log pending manager approval
        if (medication.isControlledSubstance()) {
            ControlledSubstanceLog log = new ControlledSubstanceLog();
            log.setPrescriptionUpload(saved);
            log.setPrescribingDoctor(dto.getDoctorName());
            log.setPendingApproval(true);
            controlledLogRepository.save(log);
        }

        return saved;
    }

    public List<PrescriptionUpload> getAll() {
        return prescriptionRepo.findAll();
    }

    public List<PrescriptionUpload> getByPatient(Long patientId) {
        return prescriptionRepo.findByPatientId(patientId);
    }

    public PrescriptionUpload getById(Long id) {
        return prescriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }

    public PrescriptionUpload verify(Long id, VerificationDTO dto) {
        PrescriptionUpload p = getById(id);

        if (p.getStatus() != PrescriptionStatus.UPLOADED && p.getStatus() != PrescriptionStatus.UNDER_VERIFICATION) {
            throw new RuntimeException("Prescription cannot be verified in current status: " + p.getStatus());
        }

        // check 30-day validity
        if (p.getUploadedAt().plusDays(30).isBefore(LocalDateTime.now())) {
            p.setStatus(PrescriptionStatus.EXPIRED);
            prescriptionRepo.save(p);
            throw new RuntimeException("Prescription has expired (older than 30 days)");
        }

        User pharmacist = userRepository.findById(dto.getPharmacistId())
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        p.setStatus(PrescriptionStatus.VERIFIED);
        prescriptionRepo.save(p);

        VerificationLog log = new VerificationLog();
        log.setPrescriptionUpload(p);
        log.setVerifiedBy(pharmacist);
        log.setVerificationResult("VERIFIED");
        verificationLogRepository.save(log);

        return p;
    }

    public PrescriptionUpload reject(Long id, VerificationDTO dto) {
        PrescriptionUpload p = getById(id);

        User pharmacist = userRepository.findById(dto.getPharmacistId())
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        p.setStatus(PrescriptionStatus.REJECTED);
        prescriptionRepo.save(p);

        VerificationLog log = new VerificationLog();
        log.setPrescriptionUpload(p);
        log.setVerifiedBy(pharmacist);
        log.setVerificationResult("REJECTED");
        log.setRejectionReason(dto.getRejectionReason());
        verificationLogRepository.save(log);

        return p;
    }

    public PrescriptionUpload approve(Long id, Long managerId) {
        PrescriptionUpload p = getById(id);

        if (p.getStatus() != PrescriptionStatus.VERIFIED) {
            throw new RuntimeException("Prescription must be VERIFIED before approval");
        }

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (manager.getRole() != Role.PHARMACY_MANAGER) {
            throw new RuntimeException("Only pharmacy managers can approve prescriptions");
        }

        p.setStatus(PrescriptionStatus.APPROVED);
        p.setApprovedAt(LocalDateTime.now());
        prescriptionRepo.save(p);

        // update controlled substance log
        List<ControlledSubstanceLog> logs = controlledLogRepository
                .findByPrescriptionUploadPatientId(p.getPatient().getId());
        logs.stream()
                .filter(l -> l.getPrescriptionUpload().getId().equals(id) && l.isPendingApproval())
                .forEach(l -> {
                    l.setApprovingManager(manager);
                    l.setApprovedAt(LocalDateTime.now());
                    l.setPendingApproval(false);
                    controlledLogRepository.save(l);
                });

        return p;
    }
}
