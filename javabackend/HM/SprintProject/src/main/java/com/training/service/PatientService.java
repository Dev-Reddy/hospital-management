package com.training.service;

import java.util.List;

import com.training.dto.PatientSummaryDto;
import com.training.entity.Patient;

public interface PatientService {

    Patient savePatient(Patient patient);

    Patient getPatientBySsn(Integer ssn);

    List<Patient> getAllPatients();

    void deletePatient(Integer ssn);

    List<PatientSummaryDto> getPatientSummaries(int limit);
}
