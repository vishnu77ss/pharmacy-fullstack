package com.pharmacy.controller;

import com.pharmacy.entity.ControlledSubstanceLog;
import com.pharmacy.service.ControlledSubstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controlled-logs")
@RequiredArgsConstructor
public class ControlledSubstanceController {

    private final ControlledSubstanceService controlledSubstanceService;

    @GetMapping
    public ResponseEntity<List<ControlledSubstanceLog>> getAll() {
        return ResponseEntity.ok(controlledSubstanceService.getAll());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ControlledSubstanceLog>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(controlledSubstanceService.getByPatient(patientId));
    }

    @GetMapping("/pending-approval")
    public ResponseEntity<List<ControlledSubstanceLog>> getPending() {
        return ResponseEntity.ok(controlledSubstanceService.getPendingApproval());
    }
}
