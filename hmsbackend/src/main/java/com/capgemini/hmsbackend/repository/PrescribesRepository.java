package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Prescribes;
import com.capgemini.hmsbackend.entity.PrescribesId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescribesRepository extends JpaRepository<Prescribes, PrescribesId> {
    // additional queries can be added later

    // find all prescriptions belonging to a patient (patient.ssn)
    List<Prescribes> findByPatientSsn(Integer ssn);
}
