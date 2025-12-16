package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.MedicationDto;
import com.capgemini.hmsbackend.entity.Medication;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationServiceImpl implements IMedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    public String addMedication(MedicationDto dto) {
        Medication m = new Medication();
        // Only set code if caller provided one; otherwise DB will generate it

        m.setName(dto.getName());
        m.setBrand(dto.getBrand());
       
        medicationRepository.save(m);
        return "Medication created";
    }

    @Override
    public List<MedicationDto> getAllMedications() {
        List<Medication> list = medicationRepository.findAll();
        return list.stream().map(m -> new MedicationDto(m.getCode(), m.getName(), m.getBrand())).toList();
    }

    @Override
    public Optional<MedicationDto> getByCode(Integer code) {
        return medicationRepository.findById(code).map(m -> new MedicationDto(m.getCode(), m.getName(), m.getBrand()));
    }

    @Override
    public MedicationDto updateMedication(Integer code, MedicationDto dto) {
        Medication med = medicationRepository.findById(code).orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + code));
        // update fields
        med.setName(dto.getName());
        med.setBrand(dto.getBrand());
        
        Medication updated = medicationRepository.save(med);
        return new MedicationDto(updated.getCode(), updated.getName(), updated.getBrand());
    }

    @Override
    public String deleteMedicationByCode(Integer code) {
        if (code == null) throw new IllegalArgumentException("Code required");
        boolean exists = medicationRepository.existsById(code);
        if (!exists) throw new ResourceNotFoundException("Medication not found: " + code);
        medicationRepository.deleteById(code);
        return "Medication deleted";
    }
}
