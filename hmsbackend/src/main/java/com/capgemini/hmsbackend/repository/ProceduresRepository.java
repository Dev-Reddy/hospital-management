package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.entity.Procedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProceduresRepository extends JpaRepository<Procedures, Integer> {

    Optional<Procedures> findByName(String name);

}