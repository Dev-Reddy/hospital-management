
package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.AffiliatedWith;
import com.capgemini.hmsbackend.entity.AffiliatedWithId;
import com.capgemini.hmsbackend.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

    List<AffiliatedWith> findByPhysician_EmployeeId(Integer physicianId);

    @Query("SELECT COUNT(DISTINCT a.physician) FROM AffiliatedWith a WHERE a.department.departmentId = :deptId AND a.primaryAffiliation = true")
    int countPrimaryPhysicians(@Param("deptId") int deptId);
    //code by sarvadnya
    @Query("SELECT a.department FROM AffiliatedWith a WHERE a.physician.employeeId = :physicianId")
    List<com.capgemini.hmsbackend.entity.Department> findDepartmentsByPhysicianId(@Param("physicianId") Integer physicianId);
    @Query("SELECT a.physician FROM AffiliatedWith a WHERE a.department.departmentId = :deptId")
    List<Physician> findPhysiciansByDepartment(@Param("deptId") Integer deptId);
}
