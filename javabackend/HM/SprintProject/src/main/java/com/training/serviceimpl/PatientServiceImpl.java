package com.training.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.training.dto.PatientSummaryDto;
import com.training.entity.Patient;
import com.training.exception.ResourceNotFoundException;
import com.training.repo.PatientRepository;
import com.training.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientBySsn(Integer ssn) {
        return patientRepository.findById(ssn)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "ssn", ssn));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public void deletePatient(Integer ssn) {
        if (!patientRepository.existsById(ssn)) {
            throw new ResourceNotFoundException("Patient", "ssn", ssn);
        }
        patientRepository.deleteById(ssn);
    }

    @Override
    public List<PatientSummaryDto> getPatientSummaries(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return patientRepository.findAll(pageable)
                .map(patient -> new PatientSummaryDto(patient.getSsn(), patient.getName()))
                .getContent();
    }
}
