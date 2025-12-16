package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.MedicationDto;
import com.capgemini.hmsbackend.service.IMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medication")
public class MedicationController {

    @Autowired
    private IMedicationService medicationService;

    @PostMapping
    public ResponseEntity<Map<String, String>> addMedication(@RequestBody MedicationDto dto) {
        String msg = medicationService.addMedication(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", msg));
    }

    @GetMapping
    public ResponseEntity<List<MedicationDto>> getAll() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{code}")
    public ResponseEntity<MedicationDto> getByCode(@PathVariable Integer code) {
        return medicationService.getByCode(code)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{code}")
    public ResponseEntity<MedicationDto> update(@PathVariable Integer code, @RequestBody MedicationDto dto) {
        MedicationDto updated = medicationService.updateMedication(code, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable Integer code) {
        String msg = medicationService.deleteMedicationByCode(code);
        return ResponseEntity.ok(Map.of("message", msg));
    }
}
