package com.pharmacy.controller;

import com.pharmacy.dto.ApprovalDTO;
import com.pharmacy.dto.PrescriptionDTO;
import com.pharmacy.dto.VerificationDTO;
import com.pharmacy.entity.PrescriptionUpload;
import com.pharmacy.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/upload")
    public ResponseEntity<PrescriptionUpload> upload(@Valid @RequestBody PrescriptionDTO dto) {
        return ResponseEntity.ok(prescriptionService.upload(dto));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionUpload>> getAll() {
        return ResponseEntity.ok(prescriptionService.getAll());
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<PrescriptionUpload>> getByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getByPatient(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionUpload> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getById(id));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<PrescriptionUpload> verify(@PathVariable Long id, @RequestBody VerificationDTO dto) {
        return ResponseEntity.ok(prescriptionService.verify(id, dto));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PrescriptionUpload> reject(@PathVariable Long id, @RequestBody VerificationDTO dto) {
        return ResponseEntity.ok(prescriptionService.reject(id, dto));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PrescriptionUpload> approve(@PathVariable Long id, @RequestBody ApprovalDTO dto) {
        return ResponseEntity.ok(prescriptionService.approve(id, dto.getManagerId()));
    }
}
