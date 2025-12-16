package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.NurseDTOBySahithi;
import com.capgemini.hmsbackend.dto.NurseDtoBySarv;
import com.capgemini.hmsbackend.entity.Nurse;

import java.util.List;

public interface INurseService {
    boolean isRegistered(Integer empId);

    NurseDTO getNurseDTOById(Integer empId);

    String getPositionById(Integer empId);

    List<NurseDTO> getAllNurses();
    NurseDtoBySarv createNurse(NurseDtoBySarv dto);

     NurseDTO updateRegisteredStatus(Integer empId, boolean registered);
     //code by Arshiya
     List<NurseDTO> getNursesByPatientId(Integer patientId);

    NurseDTOBySahithi getNurseById(Integer empId);
    NurseDTOBySahithi updateSSN(Integer empId, Integer ssn);

    // Update multiple nurse fields (name, position, registered, ssn)
    NurseDTOBySahithi updateNurse(Integer empId, com.capgemini.hmsbackend.dto.NurseDtoBySarv dto);

}