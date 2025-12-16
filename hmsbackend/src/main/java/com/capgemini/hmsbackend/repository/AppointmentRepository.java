package com.capgemini.hmsbackend.repository;


import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    @Query("SELECT DISTINCT CAST(a.starto AS date) FROM Appointment a WHERE a.patient.ssn = :patientId")
    List<java.sql.Date> findDistinctAppointmentDatesByPatient(@Param("patientId") Integer patientId);
    @Query("SELECT a.patient FROM Appointment a WHERE a.prepNurse.employeeId= :nurseId AND a.patient.ssn = :patientId")
    Optional<Patient> findPatientByNurseAndPatient(@Param("nurseId") Integer nurseId, @Param("patientId") Integer patientId);

    @Query("""
    SELECT r FROM Room r
    JOIN Stay s ON s.room.roomNumber = r.roomNumber
    JOIN Appointment a ON s.patient.ssn = a.patient.ssn
    WHERE a.appointmentId = :appointmentId
    ORDER BY s.stayStart ASC
""")

    Room findRoomByAppointmentId(@Param("appointmentId") Integer appointmentId);

    List<Appointment> findByPrepNurseEmployeeId(Integer nurseId);

    @Query("SELECT a FROM Appointment a WHERE a.patient.ssn = :patientId AND CAST(a.starto AS date) = :date")
    List<Appointment> findAppointmentsByPatientAndDate(@Param("patientId") Integer patientId,
                                                       @Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.prepNurse.employeeId = :nurseId AND CAST(a.starto AS date) = :date")
    List<Appointment> findAppointmentsByNurseAndDate(@Param("nurseId") Integer nurseId,
                                                     @Param("date") LocalDate date);


    //code by sarvadnya
    // New: find patients by prep nurse on a given date
    @Query("SELECT p FROM Appointment a JOIN a.patient p WHERE a.prepNurse.employeeId = :nurseId AND CAST(a.starto AS date) = :date")
    List<Patient> findPatientsByPrepNurseIdAndDate(@Param("nurseId") int nurseId, @Param("date") java.sql.Date date);

    @Query("SELECT p FROM Appointment a JOIN a.patient p WHERE a.physician.employeeId = :id")
    List<Patient> findPatientsByPhysicianId(@Param("id") int id);

    // New: find a patient by physician and patient id
    @Query("SELECT p FROM Appointment a JOIN a.patient p WHERE a.physician.employeeId = :physicianId AND p.ssn = :patientId")
    Optional<Patient> findPatientByPhysicianAndPatientId(@Param("physicianId") Integer physicianId, @Param("patientId") Integer patientId);

    @Query("SELECT a FROM Appointment a WHERE CAST(a.starto AS date) = :startDate")
    List<Appointment> findByStartDate(@Param("startDate") java.sql.Date startDate);


    @Query("SELECT a.prepNurse FROM Appointment a WHERE a.appointmentId = :appointmentId")
    Optional<Nurse> findPrepNurseByAppointmentId(@Param("appointmentId") Integer appointmentId);


    @Query("""
        SELECT DISTINCT r
        FROM Room r
        JOIN Stay s ON s.room.roomNumber = r.roomNumber
        JOIN Appointment a ON a.patient.ssn = s.patient.ssn
        WHERE a.prepNurse.employeeId = :nurseId
          AND CAST(a.starto AS date) = :date
        ORDER BY r.roomNumber ASC
    """)
    List<Room> findRoomsByNurseAndDate(@Param("nurseId") Integer nurseId,
                                       @Param("date") LocalDate date);
    //code by Arshiya
    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.physician.employeeId = :physicianId")
    List<Patient> findPatientsByPhysicianId(@Param("physicianId") Integer physicianId);

    @Query("SELECT DISTINCT a.prepNurse FROM Appointment a WHERE a.patient.ssn = :patientId AND a.prepNurse IS NOT NULL")
    List<Nurse> findNursesByPatientId(Integer patientId);

    @Query("SELECT DISTINCT new com.capgemini.hmsbackend.dto.PatientDto(p.ssn, p.name, p.address, p.phone, p.insuranceId, p.pcp.employeeId) " +
            "FROM Appointment a JOIN a.patient p WHERE a.physician.employeeId = :physicianId AND p.ssn = :patientId")
    PatientDto findPatientByPhysicianAndPatient(Integer physicianId, Integer patientId);
    //Sahithi

    @Query("""
    SELECT r FROM Room r
    JOIN Stay s ON s.room.roomNumber = r.roomNumber
    WHERE s.patient.ssn = :patientId AND CAST(s.stayStart AS date) <= :date AND CAST(s.stayEnd AS date) >= :date
""")
    Room findRoomByPatientAndDate(@Param("patientId") Integer patientId,
                                  @Param("date") LocalDate date);


    @Query("SELECT a.physician FROM Appointment a WHERE a.patient.ssn = :patientId AND CAST(a.starto AS date) = :date")
    Physician findPhysiciansByPatientAndDate(@Param("patientId") Integer patientId,
                                             @Param("date") LocalDate date);

    /** 🔹 Patients checked by a physician on a given date */
    @Query("SELECT a.patient FROM Appointment a WHERE a.physician.employeeId = :physicianId AND CAST(a.starto AS date) = :date")
    List<Patient> findPatientsByPhysicianAndDate(@Param("physicianId") Integer physicianId,
                                                 @Param("date") LocalDate date);

    @Query("SELECT a.patient FROM Appointment a WHERE a.appointmentId = :appointmentId")
    Optional<Patient> findPatientByAppointmentId(@Param("appointmentId") Integer appointmentId);


}
