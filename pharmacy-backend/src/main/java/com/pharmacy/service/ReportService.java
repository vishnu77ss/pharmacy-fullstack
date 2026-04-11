package com.pharmacy.service;

import com.pharmacy.repository.DispensingRecordRepository;
import com.pharmacy.repository.DrugInteractionCheckRepository;
import com.pharmacy.repository.PrescriptionUploadRepository;
import com.pharmacy.repository.RefillHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportService {

    private final DispensingRecordRepository dispensingRepo;
    private final DrugInteractionCheckRepository interactionRepo;
    private final PrescriptionUploadRepository prescriptionRepo;
    private final RefillHistoryRepository refillHistoryRepository;

    public ReportService(
            DispensingRecordRepository dispensingRepo,
            DrugInteractionCheckRepository interactionRepo,
            PrescriptionUploadRepository prescriptionRepo,
            RefillHistoryRepository refillHistoryRepository) {
        this.dispensingRepo = Objects.requireNonNull(dispensingRepo, "dispensingRepo cannot be null");
        this.interactionRepo = Objects.requireNonNull(interactionRepo, "interactionRepo cannot be null");
        this.prescriptionRepo = Objects.requireNonNull(prescriptionRepo, "prescriptionRepo cannot be null");
        this.refillHistoryRepository = Objects.requireNonNull(refillHistoryRepository, "refillHistoryRepository cannot be null");
    }

    public Map<String, Object> getReports() {
        Map<String, Object> report = new HashMap<>();

        List<Object[]> monthlyDispensing = dispensingRepo.countByMonth();
        report.put("monthlyDispensing", monthlyDispensing);

        List<Object[]> prescriptionsByStatus = prescriptionRepo.countByStatus();
        report.put("prescriptionsByStatus", prescriptionsByStatus);

        report.put("totalInteractions", interactionRepo.count());
        report.put("totalRefills", refillHistoryRepository.count());
        report.put("totalDispensing", dispensingRepo.count());

        return report;
    }
}
