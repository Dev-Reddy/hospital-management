package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.PhysicianCreateDTO;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PhysicianServiceImpl implements IPhysicianService{


        @Autowired
        private PhysicianRepository physicianRepository;

    @Override


        public Physician getPhysicianByEmployeeId(Integer employeeId) {
            return physicianRepository.findByEmployeeId(employeeId);
        }


    @Override
    public List<PhysicianDTO> getAllPhysician() {
        return physicianRepository.findAll()
                .stream()
                .map(nurse -> new PhysicianDTO(
                        nurse.getEmployeeId(),
                        nurse.getName(),
                        nurse.getPosition(),
                        nurse.getSsn()
                ))
                .toList();
    }

    @Override
    public List<Physician> findByNameIgnoreCase(String name) {
        return physicianRepository.findByNameIgnoreCase(name);
    }
    @Override
    @Cacheable(value = "physiciansByPosition", key = "#position")
    public List<PhysicianDTO>  findByPosition(String position) {

        List<Physician> physicians = physicianRepository.findByPosition(position);
        System.out.println("Coming from database -> physicians:"+physicians);
        return physicians.stream()
                .map(p -> new PhysicianDTO(p.getEmployeeId(), p.getName(), p.getPosition(), p.getSsn()))
                .toList();

    }
    // create physician by default id
    @Override
    @Transactional
    @CachePut(value = "physicians", key = "#result.employeeId")

    public PhysicianDTO createPhysician(@Valid PhysicianCreateDTO req) {

        physicianRepository.findBySsn(req.getSsn()).ifPresent(p -> {
            throw new DataIntegrityViolationException("SSN already exists");
        });

        Physician entity = new Physician();
        entity.setName(req.getName());
        entity.setPosition(req.getPosition());
        entity.setSsn(req.getSsn());

        Physician saved = physicianRepository.save(entity);

        // Return read DTO including employeeId so views/templates can render it
        return new PhysicianDTO(
                saved.getEmployeeId(),
                saved.getName(),
                saved.getPosition(),
                saved.getSsn()
        );
    }

    @Override
    public PhysicianDTO updatePhysicianSSN(Integer empId, Integer ssn) {
        Physician physician = physicianRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Physician not found with ID: " + empId));

        physician.setSsn(ssn);
        Physician updatedPhysician = physicianRepository.save(physician);

        return new PhysicianDTO(
                updatedPhysician.getEmployeeId(),
                updatedPhysician.getName(),
                updatedPhysician.getPosition(),
                updatedPhysician.getSsn()
        );
    }
//code by Arshiya
public PhysicianDTO updatePhysicianName(Integer empId, String newName) {

    Physician physician = physicianRepository.findById(empId)
            .orElseThrow(() -> new RuntimeException("Physician not found with ID: " + empId));

    physician.setName(newName);
    Physician updatedPhysician = physicianRepository.save(physician);

    // Directly create DTO without mapper
    return new PhysicianDTO(
            updatedPhysician.getEmployeeId(),
            updatedPhysician.getName(),
            updatedPhysician.getPosition(),
            updatedPhysician.getSsn()
    );
}
    @Override
    public void updatePosition(Integer employeeId, String newPosition) {
        Physician physician = physicianRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Physician not found with ID: " + employeeId));

        physician.setPosition(newPosition);
        physicianRepository.save(physician);
    }


}


