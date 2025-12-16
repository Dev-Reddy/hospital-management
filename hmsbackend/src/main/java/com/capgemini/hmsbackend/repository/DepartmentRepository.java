package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findByHead_EmployeeId(Integer employeeId);

    boolean existsByHead_EmployeeId(Integer employeeId);


//    @Query("SELECT d FROM Department d WHERE d.head.employeeId = :employeeId")
//    List<Department> findByHeadId(@Param("employeeId") Integer employeeId);



    // findById(Integer id) is already available

}
