package com.pharmacy.service;

import com.pharmacy.repository.DispensingRecordRepository;
import com.pharmacy.repository.DrugInteractionCheckRepository;
import com.pharmacy.repository.PrescriptionUploadRepository;
import com.pharmacy.repository.RefillHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final DispensingRecordRepository dispensingRepo;
    private final DrugInteractionCheckRepository interactionRepo;
    private final PrescriptionUploadRepository prescriptionRepo;
    private final RefillHistoryRepository refillHistoryRepository;

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
