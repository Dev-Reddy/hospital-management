package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.dto.DepartmentDTOBySahithi;
import com.capgemini.hmsbackend.entity.Department;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.exception.DepartmentNotFoundException;
import com.capgemini.hmsbackend.exception.EmployeeIdNotFoundException;
import com.capgemini.hmsbackend.repository.DepartmentRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentServiceImpl implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PhysicianRepository  physicianRepository;

    public DepartmentDTOBySahithi departmentEntityToDto(Department d) {
        if (d == null) return null;
        return DepartmentDTOBySahithi.builder()
                .departmentId(d.getDepartmentId())
                .name(d.getName())
                .headId(d.getHead() != null ? d.getHead().getEmployeeId() : null)
                .headName(d.getHead() != null ? d.getHead().getName() : null)
                .build();
    }

    @Override
    @Cacheable(value = "departments", key = "#id")
    public DepartmentDTO getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " not found"));
        return DepartmentDTO.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .build();
    }

    @Override
//    @Cacheable(value = "allDepartments")
    public List<DepartmentDTOBySahithi> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(dept -> new DepartmentDTOBySahithi(dept.getDepartmentId(), dept.getName(), dept.getHead() != null ? dept.getHead().getEmployeeId() : null, dept.getHead() != null ? dept.getHead().getName() : null))
                .toList();
    }


    @Override

    @Cacheable(
            value = "departmentsByHead",
            key = "#headId"
    )

    public List<DepartmentDTO> getDepartmentsByHeadId(Integer headId) {
        List<Department> departments = departmentRepository.findByHead_EmployeeId(headId);

        if (departments.isEmpty()) {
            throw new DepartmentNotFoundException("No departments found for head (Physician) ID: " + headId);
        }

        return departments.stream()
                .map(d -> new DepartmentDTO(
                        d.getDepartmentId(),
                        d.getName()))
                .toList();
    }

    @Override
    @Cacheable(value = "isPhysicianHead", key = "#physicianEmployeeId")
    public boolean isPhysicianHead(Integer physicianEmployeeId) {
        return departmentRepository.existsByHead_EmployeeId(physicianEmployeeId);
    }


    @Override
    @Transactional
    @CachePut(value = "departments", key = "#deptId")
    public DepartmentDTO updateDeptName(Integer deptId, String newDeptName) {
        Department dept = departmentRepository.findById(deptId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + deptId + " not found"));

        dept.setName(newDeptName.trim());
        Department saved = departmentRepository.save(dept);

        return DepartmentDTO.builder()
                .departmentId(saved.getDepartmentId())
                .name(saved.getName())
                .build();


    }

    @Override
    public DepartmentDTO updateDepartmentHead(Integer deptId, Integer headEmpId) {
        Department d = departmentRepository.findById(deptId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + deptId + " not found"));
        Physician head = physicianRepository.findById(headEmpId)
                .orElseThrow(() -> new EmployeeIdNotFoundException("Physician (head) not found with ID: " + headEmpId));
        d.setHead(head);
        Department saved = departmentRepository.save(d);
        return DepartmentDTO.builder()
                .departmentId(saved.getDepartmentId())
                .name(saved.getName())
                .build();
    }
    //By sahithi
    @Override
    @Transactional
    public DepartmentDTOBySahithi createDepartment(DepartmentDTOBySahithi departmentDTO) {
        if (departmentDTO == null || departmentDTO.getName() == null || departmentDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Department name cannot be empty");
        }
        if (departmentDTO.getHeadId() == null || departmentDTO.getHeadId() <= 0) {
            throw new IllegalArgumentException("Head (Physician employeeId) is required");
        }

        // Fetch Physician entity
        Physician head = physicianRepository.findByEmployeeId(departmentDTO.getHeadId());

        if (head == null) {
            throw new DepartmentNotFoundException(
                    "Physician head with employeeId " + departmentDTO.getHeadId() + " not found");
        }
        Department department = new Department();
//        department.setDepartmentId(departmentDTO.getDepartmentId());
        department.setName(departmentDTO.getName().trim());
        department.setHead(head); // ✅ Set head before saving

        Department saved = departmentRepository.save(department);

        return departmentEntityToDto(saved);
    }
    public DepartmentDTOBySahithi findById(Integer deptid) {
        return departmentRepository.findById(deptid)
                .map(dept -> departmentEntityToDto(dept))
                .orElse(null);
    }

}
