
package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;


import com.capgemini.hmsbackend.controller.PhysicianController;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PhysicianRepository extends JpaRepository<Physician, Integer> {

    Physician findByEmployeeId(Integer employeeId);

    boolean existsByEmployeeId(Integer employeeId);

    List<Physician> findByNameIgnoreCase(String name);
    @Query("select p from Physician p where lower(p.position) = lower(:position)")
    List<Physician> findByPosition(@Param("position") String position);
    Optional<Physician> findBySsn(Integer ssn);

    @Query("""
        select p from Physician p
        where lower(coalesce(p.name, '')) like lower(concat('%', :name, '%'))
           or lower(coalesce(p.position, '')) like lower(concat('%', :name, '%'))
    """)
    List<Physician> searchByNameOrSpeciality(String name);


}
