package com.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.dto.PatientSummaryDto;
import com.training.entity.Patient;
import com.training.service.PatientService;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/patients")
@Validated
public class PatientController {

    @Autowired
    private PatientService patientService;

    // --- Create new patient
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    // --- Get patient by SSN
    @GetMapping("/{ssn}")
    public Patient getPatient(@PathVariable("ssn") Integer ssn) {
        return patientService.getPatientBySsn(ssn);
    }

    @GetMapping(params = "limit")
    public List<PatientSummaryDto> getPatientsLimited(@RequestParam("limit") @Min(1) int limit) {
        return patientService.getPatientSummaries(limit);
    }

    // --- Get all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // --- Delete patient by SSN
    @DeleteMapping("/{ssn}")
    public String deletePatient(@PathVariable("ssn") Integer ssn) {
        patientService.deletePatient(ssn);
        return "Patient deleted successfully";
    }
}
