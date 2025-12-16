package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PrescribesDto;

import java.util.List;
import java.util.Optional;

public interface IPrescribesService {
    String addPrescribes(PrescribesDto dto);
    List<PrescribesDto> getAllPrescriptions();
    Optional<PrescribesDto> getPrescriptionById(Integer physicianId, Integer patientId, Integer medicationId, java.time.LocalDateTime date);
    PrescribesDto updatePrescription(Integer physicianId, Integer patientId, Integer medicationId, java.time.LocalDateTime date, PrescribesDto dto);
    List<PrescribesDto> getPrescriptionsForPatient(Integer patientSsn);
}
