package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.MedicationDto;

import java.util.List;
import java.util.Optional;

public interface IMedicationService {
    String addMedication(MedicationDto dto);
    List<MedicationDto> getAllMedications();
    Optional<MedicationDto> getByCode(Integer code);
    MedicationDto updateMedication(Integer code, MedicationDto dto);
    String deleteMedicationByCode(Integer code);
}
