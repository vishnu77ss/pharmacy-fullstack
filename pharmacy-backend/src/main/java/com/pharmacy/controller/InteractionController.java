package com.pharmacy.controller;

import com.pharmacy.entity.DrugInteractionCheck;
import com.pharmacy.enums.Severity;
import com.pharmacy.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    @GetMapping("/check/{prescriptionId}")
    public ResponseEntity<List<DrugInteractionCheck>> check(@PathVariable Long prescriptionId) {
        return ResponseEntity.ok(interactionService.checkForPrescription(prescriptionId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DrugInteractionCheck>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(interactionService.getByPatient(patientId));
    }

    @PostMapping("/add/{prescriptionId}")
    public ResponseEntity<DrugInteractionCheck> addInteraction(
            @PathVariable Long prescriptionId,
            @RequestBody Map<String, String> body) {
        String med = body.get("interactingMedication");
        Severity severity = Severity.valueOf(body.get("severity"));
        String recommendation = body.get("recommendation");
        return ResponseEntity.ok(interactionService.addInteraction(prescriptionId, med, severity, recommendation));
    }
}
