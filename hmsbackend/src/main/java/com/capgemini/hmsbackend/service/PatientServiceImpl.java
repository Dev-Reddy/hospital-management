
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.PatientDtoBySarv;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.AppointmentRepository;
import com.capgemini.hmsbackend.repository.PatientRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
//import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.exception.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;
    private final PhysicianRepository physicianRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;


    @Override
    public PatientDto getPatientCheckedByPhysician(Integer physicianId, Integer patientId) {
        if (!physicianRepository.existsByEmployeeId(physicianId)) {
            throw new ResourceNotFoundException("Physician with ID " + physicianId + " not found.");
        }

        Patient patient = patientRepository.findPatientCheckedByPhysician(physicianId, patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with ID " + patientId + " was not checked by Physician " + physicianId));

        return toDto(patient);
    }


    private PatientDto toDto(Patient p) {
        PatientDto dto = new PatientDto();
        dto.setSsn(p.getSsn());
        dto.setName(p.getName());
        dto.setAddress(p.getAddress());
        dto.setPhone(p.getPhone());
        dto.setInsuranceId(p.getInsuranceId());
        return dto;
    }



    public PatientDto patientEntityToDto(Patient patient) {
        if (patient == null) return null;
        return PatientDto.builder()
                .ssn(patient.getSsn())
                .name(patient.getName())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .insuranceId(patient.getInsuranceId())
                .build();
    }

    @Override
    @Cacheable(value = "insuranceByPatient", key = "#patientId")
    public Optional<Integer> getInsuranceIdByPatientId(Integer patientId) {
        System.out.println("insuranceByPatient from database");
        return patientRepository.findInsuranceIdByPatientId(patientId);
    }

    @Override
    @Transactional
    @CachePut(value = "patients", key = "#ssn")
    public PatientDto updatePatientAddressBySsn(Integer ssn, String newAddress) {
        Patient patient = patientRepository.findBySsn(ssn)
                .orElseThrow(() -> new PatientNotFoundException("Patient not fount with patient id " + ssn));

        patient.setAddress(newAddress.trim());
        Patient saved = patientRepository.save(patient);

        return PatientDto.builder()
                .ssn(saved.getSsn())
                .name(saved.getName())
                .address(saved.getAddress())
                .phone(saved.getPhone())
                .insuranceId(saved.getInsuranceId())
                .build();

    }



    @Override
    public List<PatientDto> getPatientsCheckedByNurse(Integer nurseId) {
        List<Patient> patients = patientRepository.findPatientsCheckedByNurse(nurseId);
        return patients.stream()

                .map(p -> PatientDto.builder()
                        .ssn(p.getSsn())
                        .name(p.getName())
                        .address(p.getAddress())
                        .phone(p.getPhone())
                        .insuranceId(p.getInsuranceId())
                        .build())
                .toList();
    }

    //code by sarvadnya

    public PatientDtoBySarv update(Integer ssn, PatientDtoBySarv dto) {
        Patient patient = patientRepository.findById(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with SSN: " + ssn));

        if (dto.getName() != null) patient.setName(dto.getName());
        if (dto.getAddress() != null) patient.setAddress(dto.getAddress());
        if (dto.getPhone() != null) patient.setPhone(dto.getPhone());
        if (dto.getInsuranceId() != null) patient.setInsuranceId(dto.getInsuranceId());

        // Allow updating PCP if provided
        if (dto.getPcpId() != null) {
            Physician pcp = fetchPhysician(dto.getPcpId());
            patient.setPcp(pcp);
        }

        Patient saved = patientRepository.save(patient);
        return toDtoBySarv(saved);
    }

    private PatientDtoBySarv toDtoBySarv(Patient entity) {
        return PatientDtoBySarv.builder()
                .ssn(entity.getSsn())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .insuranceId(entity.getInsuranceId())
                .pcpId(entity.getPcp() != null ? entity.getPcp().getEmployeeId() : null)
                .build();
    }

    private Physician fetchPhysician(Integer pcpId) {
        if (pcpId == null) {
            throw new IllegalArgumentException("pcpId must not be null");
        }
        return physicianRepository.findById(pcpId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Physician (PCP) not found with ID: " + pcpId));
    }
    //code by arshiya
    public PatientDto getPatientByPhysicianAndPatient(Integer physicianId, Integer patientId) {
        PatientDto patientDto = appointmentRepository.findPatientByPhysicianAndPatient(physicianId, patientId);
        if (patientDto == null) {
            throw new ResourceNotFoundException("No appointment found for physician ID " + physicianId + " and patient ID " + patientId);
        }
        return patientDto;
    }
    @Override
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::patientEntityToDto)
                .toList();
    }
}
