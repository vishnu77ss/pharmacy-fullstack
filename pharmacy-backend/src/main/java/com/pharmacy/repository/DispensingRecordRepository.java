package com.pharmacy.repository;

import com.pharmacy.entity.DispensingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DispensingRecordRepository extends JpaRepository<DispensingRecord, Long> {
    List<DispensingRecord> findByPrescriptionUploadId(Long prescriptionId);

    @Query("SELECT COUNT(d), MONTH(d.dispensingDate) FROM DispensingRecord d GROUP BY MONTH(d.dispensingDate)")
    List<Object[]> countByMonth();
}
