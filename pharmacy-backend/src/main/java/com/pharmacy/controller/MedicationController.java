package com.pharmacy.controller;

import com.pharmacy.dto.MedicationDTO;
import com.pharmacy.entity.Medication;
import com.pharmacy.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<Medication> create(@Valid @RequestBody MedicationDTO dto) {
        return ResponseEntity.ok(medicationService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAll() {
        return ResponseEntity.ok(medicationService.getAll());
    }

    @GetMapping("/controlled")
    public ResponseEntity<List<Medication>> getControlled() {
        return ResponseEntity.ok(medicationService.getControlled());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getById(id));
    }
}
