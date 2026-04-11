package com.pharmacy.service;

import com.pharmacy.entity.ControlledSubstanceLog;
import com.pharmacy.repository.ControlledSubstanceLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlledSubstanceService {

    private final ControlledSubstanceLogRepository logRepository;

    public List<ControlledSubstanceLog> getAll() {
        return logRepository.findAll();
    }

    public List<ControlledSubstanceLog> getByPatient(Long patientId) {
        return logRepository.findByPrescriptionUploadPatientId(patientId);
    }

    public List<ControlledSubstanceLog> getPendingApproval() {
        return logRepository.findByPendingApprovalTrue();
    }
}
