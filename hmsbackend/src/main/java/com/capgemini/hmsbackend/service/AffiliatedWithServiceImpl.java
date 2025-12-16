package com.capgemini.hmsbackend.service;
import com.capgemini.hmsbackend.dto.AffiliatedWithDTO;
import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.AffiliatedWith;
import com.capgemini.hmsbackend.exception.InvalidDepartmentException;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.capgemini.hmsbackend.entity.AffiliatedWithId;
import com.capgemini.hmsbackend.entity.Department;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.repository.AffiliatedWithRepository;
import com.capgemini.hmsbackend.exception.EmployeeIdNotFoundException;
import com.capgemini.hmsbackend.repository.DepartmentRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AffiliatedWithServiceImpl implements IAffiliatedWithService {

    private final AffiliatedWithRepository affiliatedWithRepository;
    @Autowired
    private  PhysicianRepository physicianRepository;


    @Autowired
    private  DepartmentRepository departmentRepository;


    public AffiliatedWithServiceImpl(AffiliatedWithRepository affiliatedWithRepository) {
        this.affiliatedWithRepository = affiliatedWithRepository;
    }

    @Override
    @CacheEvict(cacheNames = {"physicianCount"}, allEntries = true)
    public boolean updatePrimaryAffiliation(Integer physicianId) {
        List<AffiliatedWith> affiliations = affiliatedWithRepository.findByPhysician_EmployeeId(physicianId);

        if (affiliations.isEmpty()) {
            throw new EmployeeIdNotFoundException("No affiliations found for Physician ID: " + physicianId);
        }

        // Reset all to false
        affiliations.forEach(a -> a.setPrimaryAffiliation(false));

        affiliations.get(0).setPrimaryAffiliation(true);

        affiliatedWithRepository.saveAll(affiliations);
        return true;
    }
    @Override
    @Cacheable(cacheNames = "physicianCount", key = "#departmentId")
    public int getPhysicianCount(int departmentId) {

        if (departmentId <= 0) {
           throw new InvalidDepartmentException("Department id is not valid");
        }

        int count = affiliatedWithRepository.countPrimaryPhysicians(departmentId);

        if (count == 0) {
            throw new ResourceNotFoundException("No physicians found for department ID: " + departmentId);
        }
        System.out.println("Response of getPhysicianCount is coming from database ");


        return count;
    }


    //code by sarvadnya

    @Override
    public List<DepartmentDTO> getDepartmentsByPhysician(Integer physicianId) {
        List<Department> depts = affiliatedWithRepository.findDepartmentsByPhysicianId(physicianId);
        return depts.stream().map(d -> new DepartmentDTO(
                d.getDepartmentId(),
                d.getName())).toList();
    }
    //code by Arshiya
    public void addAffiliation(AffiliatedWithDTO dto) {

        Physician physician = physicianRepository.findById(dto.getPhysicianId())
                .orElseThrow(() -> new RuntimeException("Physician not found with ID: " + dto.getPhysicianId()));
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + dto.getDepartmentId()));

        AffiliatedWith affiliation = new AffiliatedWith();
        affiliation.setId(new AffiliatedWithId(dto.getPhysicianId(), dto.getDepartmentId()));
        affiliation.setPhysician(physician);
        affiliation.setDepartment(department);
        affiliation.setPrimaryAffiliation(dto.isPrimaryAffiliation());

        affiliatedWithRepository.save(affiliation);

    }
   @Override
    public List<PhysicianDTO> getPhysiciansByDepartment(int departmentId) {
        if (departmentId <= 0) {
            throw new InvalidDepartmentException("Department id is not valid");
        }

        List<Physician> physicians = affiliatedWithRepository.findPhysiciansByDepartment(departmentId);

        if (physicians.isEmpty()) {
            throw new ResourceNotFoundException("No physicians found for department ID: " + departmentId);
        }


        return physicians.stream()
                .map(p -> new PhysicianDTO(p.getEmployeeId(),p.getPosition(),p.getName(),p.getSsn()))
                .toList();

    }
}
