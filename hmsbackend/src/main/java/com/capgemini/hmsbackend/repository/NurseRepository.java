
package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {


    @Query("SELECT n FROM Nurse n WHERE LOWER(n.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Nurse> searchNurseByName(@Param("name") String name);


}
