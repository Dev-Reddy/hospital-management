package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    // additional query methods can be added later
}

