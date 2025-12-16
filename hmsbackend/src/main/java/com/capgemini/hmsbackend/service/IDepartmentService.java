package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.dto.DepartmentDTOBySahithi;

import java.util.List;

public interface IDepartmentService {
    DepartmentDTO getDepartmentById(Integer id);
    List<DepartmentDTOBySahithi> getAllDepartments();
    boolean isPhysicianHead(Integer physicianEmployeeId);
    List<DepartmentDTO> getDepartmentsByHeadId(Integer employeeId);
  DepartmentDTO updateDeptName(Integer deptId, String newDeptName);
 //  DepartmentDTO updateDeptName(Integer deptId, String newDeptName);
 DepartmentDTOBySahithi createDepartment(DepartmentDTOBySahithi departmentDTO);

    //code by sarvadnya
    DepartmentDTO updateDepartmentHead(Integer deptId, Integer headEmpId);
}


