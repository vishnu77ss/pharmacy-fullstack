package com.pharmacy.repository;

import com.pharmacy.entity.RefillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RefillHistoryRepository extends JpaRepository<RefillHistory, Long> {
    List<RefillHistory> findByPrescriptionUploadId(Long prescriptionId);
}
