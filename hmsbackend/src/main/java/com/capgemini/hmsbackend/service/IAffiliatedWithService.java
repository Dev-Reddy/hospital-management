
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.AffiliatedWithDTO;
import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.dto.PhysicianDTO;

import java.util.List;

public interface IAffiliatedWithService {
    boolean updatePrimaryAffiliation(Integer physicianId);
    int getPhysicianCount(int departmentId);


    //code by sarvadnya
    List<DepartmentDTO> getDepartmentsByPhysician(Integer physicianId);
    //code by Arshiya
    void addAffiliation(AffiliatedWithDTO dto);
    List<PhysicianDTO> getPhysiciansByDepartment(int departmentId);
}
