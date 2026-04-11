package com.pharmacy.controller;

import com.pharmacy.dto.DispensingDTO;
import com.pharmacy.entity.DispensingRecord;
import com.pharmacy.service.DispensingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispensing")
@RequiredArgsConstructor
public class DispensingController {

    private final DispensingService dispensingService;

    @PostMapping
    public ResponseEntity<DispensingRecord> dispense(@Valid @RequestBody DispensingDTO dto) {
        return ResponseEntity.ok(dispensingService.dispense(dto));
    }

    @GetMapping("/prescription/{id}")
    public ResponseEntity<List<DispensingRecord>> getByPrescription(@PathVariable Long id) {
        return ResponseEntity.ok(dispensingService.getByPrescription(id));
    }

    @PostMapping("/refill")
    public ResponseEntity<DispensingRecord> refill(@Valid @RequestBody DispensingDTO dto) {
        return ResponseEntity.ok(dispensingService.refill(dto));
    }
}
