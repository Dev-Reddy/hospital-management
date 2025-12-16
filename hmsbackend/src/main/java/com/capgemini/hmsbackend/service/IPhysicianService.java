package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PhysicianCreateDTO;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.Physician;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface IPhysicianService {

    List<PhysicianDTO>getAllPhysician();
    Physician getPhysicianByEmployeeId(Integer employeeId);
    List<Physician> findByNameIgnoreCase(String name);

  PhysicianDTO updatePhysicianSSN(Integer empId, Integer ssn);
    List<PhysicianDTO> findByPosition(String position);
    PhysicianDTO createPhysician(@Valid PhysicianCreateDTO req);
    PhysicianDTO updatePhysicianName(Integer empId, String newName);
    void updatePosition(Integer employeeId, String newPosition);
}
