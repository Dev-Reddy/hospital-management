

package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.AppointmentDto;
import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.RoomDto;
import com.capgemini.hmsbackend.entity.Appointment;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.dto.*;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Room;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
public interface IAppointmentService{

    List<AppointmentDto> getAllAppointments();
    List<LocalDate> getAppointmentDatesByPatient(Integer patientId);
    RoomDto getExaminationRoomByAppointmentId(Integer appointmentId);
    Optional<PatientDto> getPatientCheckedByNurse(Integer nurseId, Integer patientId);
    AppointmentDto updateExaminationRoomByAppointmentId(Integer appointmentId, String examinationRoom);
    String getRoomByAppointmentId(Integer appointmentId);



    List<PatientDto> getPatientsByNurse(Integer nurseId);
   List<AppointmentDto> getPhysicianByNurseOnDate(Integer patientId, String date);


    void createAppointment(AppointmentDto appointmentDto);


    List<RoomDto> getRoomsByNurseAndDate(Integer nurseId, LocalDate date);

    NurseDTO getNurseDetailsByAppointmentId(Integer appointmentId);
    //code by sarvadnya
    List<PatientDtoBySarv> getPatientsByPrepNurseOnDate(int nurseId, java.sql.Date date);

    List<PatientDtoBySarv> getPatientsByPhysician(int physicianId);

    Optional<PatientDtoBySarv> getPatientByPhysicianAndPatientId(Integer physicianId, Integer patientId);

    List<AppointmentDtoBySarv> getAppointmentsByStartDate(java.sql.Date startDate);
    //code by arshiya
    void addPatient(PatientDto patientDto);
    List<PatientDto> getPatientsByPhysician(Integer physicianId);
    PhysicianDTO getPhysicianByAppointmentId(Integer appointmentId);
    //sahithi
    List<PatientDto> getPatientsByPhysicianOnDate(Integer physicianId, String date);
    PhysicianDTO getPhysiciansByPatientOnDate(Integer patientId, String date);
    RoomDto getRoomByPatientOnDate(Integer patientId, String date);
    Optional<PatientDto> getPatientByAppointmentId(Integer appointmentId);
    String deleteByAppointmentId(Integer id);
}

