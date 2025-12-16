package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.PrescribesDto;
import com.capgemini.hmsbackend.service.IPrescribesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescribes")
public class PrescribesController {

    @Autowired
    private IPrescribesService prescribesService;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody PrescribesDto dto) {
        // Log incoming DTO for debugging
        System.out.println("[PrescribesController.create] incoming DTO: " + dto);

        // Basic validation
        StringBuilder missing = new StringBuilder();
        if (dto == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request body is required"));
        }
        if (dto.getPhysicianId() == null) missing.append("physicianId, ");
        if (dto.getPatientId() == null) missing.append("patientId, ");
        if (dto.getMedicationId() == null) missing.append("medicationId, ");

        if (missing.length() > 0) {
            String fields = missing.toString();
            if (fields.endsWith(", ")) fields = fields.substring(0, fields.length()-2);
            System.out.println("[PrescribesController.create] missing fields: " + fields);
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields: " + fields));
        }

        // If client didn't provide a date, use now (server local)
        if (dto.getDate() == null) {
            dto.setDate(java.time.LocalDateTime.now());
        }

        try {
            String msg = prescribesService.addPrescribes(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", msg));
        } catch (com.capgemini.hmsbackend.exception.ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // Log the error server-side, but return a friendly message to client
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to create prescription", "detail", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<PrescribesDto>> list() {
        return ResponseEntity.ok(prescribesService.getAllPrescriptions());
    }

    @GetMapping("/{physicianId}/{patientId}/{medicationId}/{date}")
    public ResponseEntity<PrescribesDto> getOne(
            @PathVariable Integer physicianId,
            @PathVariable Integer patientId,
            @PathVariable Integer medicationId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        return prescribesService.getPrescriptionById(physicianId, patientId, medicationId, date)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{physicianId}/{patientId}/{medicationId}/{date}")
    public ResponseEntity<PrescribesDto> update(
            @PathVariable Integer physicianId,
            @PathVariable Integer patientId,
            @PathVariable Integer medicationId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestBody PrescribesDto dto) {
        PrescribesDto updated = prescribesService.updatePrescription(physicianId, patientId, medicationId, date, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/patient/{ssn}")
    public ResponseEntity<List<PrescribesDto>> getByPatient(@PathVariable Integer ssn) {
        List<PrescribesDto> list = prescribesService.getPrescriptionsForPatient(ssn);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
