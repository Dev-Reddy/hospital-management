package com.capgemini.hmsbackend.controller;



import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.PatientDtoBySarv;
import com.capgemini.hmsbackend.dto.PatientPhoneDto;
import com.capgemini.hmsbackend.dto.PatientUpdateRequest;
import com.capgemini.hmsbackend.exception.PatientNotFoundException;
import com.capgemini.hmsbackend.service.IAppointmentService;
import com.capgemini.hmsbackend.service.IPatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;
    private final IAppointmentService appointmentService;

    @GetMapping("/{physicianId}/{patientId}")
    public ResponseEntity<PatientDto> getPatientCheckedByPhysician(@PathVariable Integer physicianId,
                                                                   @PathVariable Integer patientId) {
        PatientDto patientDto = patientService.getPatientCheckedByPhysician(physicianId, patientId);
        return ResponseEntity.ok(patientDto); // HTTP 200 OK with body
    }

    @GetMapping("/insurance/{patientId}")
    public ResponseEntity<Object> getInsuranceIdByPatientId(@PathVariable Integer patientId) {

        if (patientId == null || patientId <= 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid Request",
                    "message", "patientId must be a positive integer."                //    "details", Map.of("patientId", patientId)
            ));
        }

        return patientService.getInsuranceIdByPatientId(patientId)
                .<ResponseEntity<Object>>map(insuranceId -> ResponseEntity.ok(Map.of(

                        "insuranceId", insuranceId
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "Patient Not Found",
                        "message", "No patient found for ID: " + patientId
                )));

    }
    @PutMapping("/address/{ssn}")
    public ResponseEntity<Object> updateAddressBySsn(
            @PathVariable Integer ssn,
            @Valid @RequestBody PatientUpdateRequest request) {

        if (ssn == null || ssn <= 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid Request",
                    "message", "SSN must be a positive integer."
            ));
        }

        // Let the global exception handler convert PatientNotFoundException to 404.
        PatientDto updated = patientService.updatePatientAddressBySsn(ssn, request.getAddress());
        return ResponseEntity.ok(updated);
    }




    @GetMapping("/patient/{nurseId}")
    public ResponseEntity<List<PatientDto>> getPatientsCheckedByNurse(@PathVariable Integer nurseId) {
        List<PatientDto> patients = patientService.getPatientsCheckedByNurse(nurseId);
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }

    //code by sarvadnya
    // PUT /api/patient/phone/{ssn} -> update only phone
    @PutMapping("/phone/{ssn}")
    public ResponseEntity<PatientDtoBySarv> updatePhone(@PathVariable Integer ssn, @Valid @RequestBody PatientPhoneDto dto) {
        PatientDtoBySarv update = new PatientDtoBySarv();
        update.setPhone(dto.getPhone());
        PatientDtoBySarv updated = patientService.update(ssn, update);
        return ResponseEntity.ok(updated);
    }
    //code by arshiya
    @PostMapping("/add")
    public ResponseEntity<Map<String,String>> addPatient(@RequestBody PatientDto patientDto) {
        appointmentService.addPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Record Created Successfully"));
    }
    @GetMapping("/{physicianId}")
    public ResponseEntity<List<PatientDto>> getPatientsByPhysician(@PathVariable Integer physicianId) {
        List<PatientDto> patients = appointmentService.getPatientsByPhysician(physicianId);
        return ResponseEntity.ok(patients);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
}
