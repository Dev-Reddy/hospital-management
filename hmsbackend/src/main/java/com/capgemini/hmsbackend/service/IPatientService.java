
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.PatientDtoBySarv;
import com.capgemini.hmsbackend.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {

   //  Patient getPatientCheckedByNurse(Long nurseId, Long patientId);
    PatientDto getPatientCheckedByPhysician(Integer physicianId, Integer patientId);
    Optional<Integer> getInsuranceIdByPatientId(Integer patientId);
    PatientDto updatePatientAddressBySsn(Integer ssn, String newAddress);


    List<PatientDto> getPatientsCheckedByNurse(Integer nurseId);

    //code by sarvadnya
    PatientDtoBySarv update(Integer ssn, PatientDtoBySarv dto);
    //arshiya
    PatientDto getPatientByPhysicianAndPatient(Integer physicianId, Integer patientId);
    List<PatientDto> getAllPatients();
}

