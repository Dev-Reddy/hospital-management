package com.capgemini.hmsbackend.service;


import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.NurseDTOBySahithi;
import com.capgemini.hmsbackend.dto.NurseDtoBySarv;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.exception.NurseNotFoundException;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.AppointmentRepository;
import com.capgemini.hmsbackend.repository.NurseRepository;
import com.capgemini.hmsbackend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NurseServiceImpl implements INurseService {

    private final NurseRepository repository;
    private final PatientRepository patientRepository;
    private static final Logger log = LoggerFactory.getLogger(NurseServiceImpl.class);

    private final AppointmentRepository appointmentRepository;

    @Override
    public boolean isRegistered(Integer empId) {
        Nurse nurse = repository.findById(empId)
                .orElseThrow(() -> new NurseNotFoundException("Nurse not found with ID: " + empId));
        return nurse.isRegistered();
    }

    @Override
    public NurseDTO getNurseDTOById(Integer empId) {
        Nurse nurse = repository.findById(empId)
                .orElseThrow(() -> new NurseNotFoundException("Nurse not found with ID: " + empId));
        return new NurseDTO(
                nurse.getEmployeeId(),
                nurse.getName(),
                nurse.getPosition(),
                nurse.isRegistered()
        );
    }

    @Override
    public String getPositionById(Integer empId) {
        Nurse nurse = repository.findById(empId)
                .orElseThrow(() -> new NurseNotFoundException("Nurse not found with ID: " + empId));
        return nurse.getPosition();
    }

    @Override
    public List<NurseDTO> getAllNurses() {
        return repository.findAll()
                .stream()
                .map(nurse -> new NurseDTO(
                        nurse.getEmployeeId(),
                        nurse.getName(),
                        nurse.getPosition(),
                        nurse.isRegistered()
                ))
                .toList();
    }

    @Override
    @Transactional
    public NurseDTO updateRegisteredStatus(Integer empId, boolean registered) {
        Nurse nurse = repository.findById(empId)
                .orElseThrow(() -> new NurseNotFoundException("Nurse not found with ID: " + empId));
        nurse.setRegistered(registered);
        Nurse updatedNurse = repository.save(nurse);

        return new NurseDTO(
                updatedNurse.getEmployeeId(),
                updatedNurse.getName(),
                updatedNurse.getPosition(),
                updatedNurse.isRegistered()
        );
    }

//    @Override
//    @Transactional
//    public NurseDTO updateRegisteredStatus(Integer empId, boolean registered) {
//        Nurse nurse = repository.findById(empId)
//                .orElseThrow(() -> new NurseNotFoundException("Nurse not found with ID: " + empId));
//        nurse.setRegistered(registered);
//        Nurse updatedNurse = repository.save(nurse);
//
//        return new NurseDTO(
//                updatedNurse.getEmployeeId(),
//                updatedNurse.getName(),
//                updatedNurse.getPosition(),
//                updatedNurse.isRegistered()
//        );
//    }


//code by sarvadnya
@Override
public NurseDtoBySarv createNurse(NurseDtoBySarv dto) {
    Nurse nurse = new Nurse();
    // If caller supplies an employeeId in the DTO, use it. This allows testing against a DB
    // where EmployeeID is not auto-incremented. If you prefer DB-generated IDs, remove this and
    // ensure the DB column is set to AUTO_INCREMENT.
    if (dto.getEmployeeId() != null) {
        nurse.setEmployeeId(dto.getEmployeeId());
    }
    nurse.setName(dto.getName());
    nurse.setPosition(dto.getPosition());
    nurse.setRegistered(dto.isRegistered());
    nurse.setSsn(dto.getSsn());
    Nurse saved = repository.save(nurse);
    return new NurseDtoBySarv(saved.getEmployeeId(), saved.getName(), saved.getPosition(), saved.isRegistered(), saved.getSsn());
}
//code by Arshiya
public List<NurseDTO> getNursesByPatientId(Integer patientId) {
    // Check if patient exists
    boolean exists = patientRepository.existsById(patientId);
    if (!exists) {
        throw new ResourceNotFoundException("Patient with ID " + patientId + " not found");
    }

    // Fetch nurses using AppointmentRepository query
    List<Nurse> nurses = appointmentRepository.findNursesByPatientId(patientId);

    // Map to DTO
    return nurses.stream()
            .map(nurse -> new NurseDTO(
                    nurse.getEmployeeId(),
                    nurse.getName(),
                    nurse.getPosition(),
                    nurse.isRegistered()
            ))
            .toList();
}
@Override
@Transactional
public NurseDTOBySahithi updateSSN(Integer empId, Integer ssn) {
    Nurse nurse = repository.findById(empId)
            .orElseThrow(() -> new ResourceNotFoundException("Nurse with ID " + empId + " not found"));

    nurse.setSsn(ssn); // Assuming Nurse entity has ssn field
    repository.save(nurse);

    return new NurseDTOBySahithi(nurse.getEmployeeId(), nurse.getName(), nurse.getPosition(), nurse.isRegistered(),nurse.getSsn());
}
@Override
@Transactional
public NurseDTOBySahithi updateNurse(Integer empId, NurseDtoBySarv dto) {
    log.info("updateNurse invoked empId={}, dto={}", empId, dto);
    Nurse nurse = repository.findById(empId)
            .orElseThrow(() -> new ResourceNotFoundException("Nurse with ID " + empId + " not found"));

    // update allowed fields (do not change employeeId)
    if (dto.getName() != null) nurse.setName(dto.getName());
    if (dto.getPosition() != null) nurse.setPosition(dto.getPosition());
    nurse.setRegistered(dto.isRegistered());
    if (dto.getSsn() != null) nurse.setSsn(dto.getSsn());
    repository.save(nurse);
    log.info("updateNurse saved nurse empId={}", empId);

    return new NurseDTOBySahithi(nurse.getEmployeeId(), nurse.getName(), nurse.getPosition(), nurse.isRegistered(), nurse.getSsn());
}

@Override
public NurseDTOBySahithi getNurseById(Integer empId) {
    Nurse nurse = repository.findById(empId)
            .orElseThrow(() -> new NurseNotFoundException("Nurse not found with ID: " + empId));
    return new NurseDTOBySahithi(
            nurse.getEmployeeId(),
            nurse.getName(),
            nurse.getPosition(),
            nurse.isRegistered(),
            nurse.getSsn()
    );
}
}
