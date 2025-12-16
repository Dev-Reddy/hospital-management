
package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



    // Search by partial name (case-insensitive)

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Override
    boolean existsById(Integer integer);

    List<Patient> findByNameContainingIgnoreCase(String namePart);
    Optional<Patient> findBySsn(Integer ssn);
     @Query("select p.insuranceId from Patient p where p.ssn = :patientId")
     Optional<Integer> findInsuranceIdByPatientId(@Param("patientId") Integer patientId);

    boolean existsBySsn(Integer ssn);

    /**
     * Returns a patient (by SSN) who has at least one appointment with the given physician (by EmployeeID).
     * Assumes:
     *  - Patient has: List<Appointment> appointments;
     *  - Appointment has: Physician physician; Patient patient;
     *  - Physician primary key field: employeeId.
     */
    @Query("""
        select p
        from Patient p
        join p.appointments a
        where p.ssn = :patientId
          and a.physician.employeeId = :physicianId
    """)
    Optional<Patient> findPatientCheckedByPhysician(
            @Param("physicianId") Integer physicianId,
            @Param("patientId") Integer patientId
    );


    @Query("""
    SELECT DISTINCT p
    FROM Patient p
    JOIN p.appointments a
    WHERE a.prepNurse.employeeId = :nurseId
""")
    List<Patient> findPatientsCheckedByNurse(@Param("nurseId") Integer nurseId);


    @Query("""
    select p from Patient p
    where lower(coalesce(p.name, '')) like lower(concat('%', :name, '%'))
       or (:id is not null and p.ssn = :id)
""")
    List<Patient> searchByNameOrExactId(@org.springframework.data.repository.query.Param("name") String name,@org.springframework.data.repository.query.Param("id") Integer id);


}
