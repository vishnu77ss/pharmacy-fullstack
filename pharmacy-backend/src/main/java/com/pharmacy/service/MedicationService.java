package com.pharmacy.service;

import com.pharmacy.dto.MedicationDTO;
import com.pharmacy.entity.Medication;
import com.pharmacy.enums.Schedule;
import com.pharmacy.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = Objects.requireNonNull(medicationRepository, "medicationRepository cannot be null");
    }

    public Medication create(MedicationDTO dto) {
        Medication m = new Medication();
        m.setMedicationName(dto.getMedicationName());
        m.setGenericName(dto.getGenericName());
        m.setDrugClass(dto.getDrugClass());
        m.setControlledSubstance(dto.isControlledSubstance());
        m.setControlledSchedule(dto.getControlledSchedule());
        m.setRequiresPrescription(dto.isRequiresPrescription());
        m.setContraindications(dto.getContraindications());

        // enforce refill rules by schedule
        if (dto.isControlledSubstance() && dto.getControlledSchedule() != null) {
            Schedule sched = dto.getControlledSchedule();
            if (sched == Schedule.II) {
                m.setMaxRefillsAllowed(0);
            } else if (sched == Schedule.III || sched == Schedule.IV) {
                m.setMaxRefillsAllowed(Math.min(dto.getMaxRefillsAllowed(), 5));
            } else {
                m.setMaxRefillsAllowed(dto.getMaxRefillsAllowed());
            }
        } else {
            m.setMaxRefillsAllowed(dto.getMaxRefillsAllowed());
        }

        return medicationRepository.save(m);
    }

    public List<Medication> getAll() {
        return medicationRepository.findAll();
    }

    public List<Medication> getControlled() {
        return medicationRepository.findByIsControlledSubstanceTrue();
    }

    public Medication getById(Long id) {
        return medicationRepository.findById(Objects.requireNonNull(id, "id cannot be null"))
                .orElseThrow(() -> new RuntimeException("Medication not found: " + id));
    }
}
