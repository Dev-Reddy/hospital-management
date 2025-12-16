
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.*;
import com.capgemini.hmsbackend.entity.*;
import com.capgemini.hmsbackend.repository.*;
import com.capgemini.hmsbackend.service.mapper.EntityMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {


    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private NurseRepository nurseRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PatientServiceImpl patientServiceImpl;

    @Mock
    private RoomServiceImpl roomServiceImpl;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("getAllAppointments() should return mapped DTO list")
    void testGetAllAppointments() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        appointment.setStarto(LocalDateTime.of(2025, 12, 2, 10, 0));
        appointment.setEndo(LocalDateTime.of(2025, 12, 2, 11, 0));

        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("John");
        appointment.setPatient(patient);

        Physician physician = new Physician();
        physician.setEmployeeId(301);
        physician.setName("Dr. Smith");
        appointment.setPhysician(physician);

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        List<AppointmentDto> result = appointmentService.getAllAppointments();
        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getPatientId());
        assertEquals("John", result.get(0).getPatientName());
        assertEquals("Dr. Smith", result.get(0).getPhysicianName());
    }


    @Test
    @DisplayName("getExaminationRoomByAppointmentId() should return RoomDto")
    void testGetExaminationRoomByAppointmentId() {
        Room room = new Room();
        room.setRoomNumber(101);
        room.setRoomType("General");

        when(appointmentRepository.findRoomByAppointmentId(1)).thenReturn(room);
        when(roomServiceImpl.roomEntityToDto(room)).thenReturn(new RoomDto(101, "General", 1, 10, false));

        RoomDto result = appointmentService.getExaminationRoomByAppointmentId(1);
        assertEquals(101, result.getRoomNumber());
        assertEquals("General", result.getRoomType());
    }

//    @Test
//    @DisplayName("getNurseDetailsByAppointmentId() should return NurseDTO")
//    void testGetNurseDetailsByAppointmentId() {
//        Nurse nurse = new Nurse();
//        nurse.setEmployeeId(201);
//        nurse.setName("Alice");
//        nurse.setPosition("Prep Nurse");
//        nurse.setRegistered(true);
//
//        when(appointmentRepository.existsById(1)).thenReturn(true);
//        when(appointmentRepository.findPrepNurseByAppointmentId(1)).thenReturn(Optional.of(nurse));
//
//        NurseDTO result = appointmentService.getNurseDetailsByAppointmentId(1);
//        assertEquals(201, result.getEmployeeId());
//        assertEquals("Alice", result.getName());
//    }


    @Test
    @DisplayName("updateExaminationRoomByAppointmentId() should update and return DTO")
    void testUpdateExaminationRoomByAppointmentId() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        appointment.setExaminationRoom("Old Room");

        appointment.setStarto(LocalDateTime.of(2025, 12, 2, 10, 0));
        appointment.setEndo(LocalDateTime.of(2025, 12, 2, 11, 0));

        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("John");
        appointment.setPatient(patient);

        Physician physician = new Physician();
        physician.setEmployeeId(301);
        physician.setName("Dr. Smith");
        appointment.setPhysician(physician);

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        AppointmentDto result = appointmentService.updateExaminationRoomByAppointmentId(1, "New Room");
        assertEquals("New Room", result.getExaminationRoom());
    }


    @Test
    @DisplayName("getPatientsByPrepNurseOnDate() should return mapped PatientDtoBySarv list")
    void testGetPatientsByPrepNurseOnDate() {
        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("John");

        when(appointmentRepository.findPatientsByPrepNurseIdAndDate(10, Date.valueOf("2025-12-02")))
                .thenReturn(List.of(patient));
        when(entityMapper.toPatientDto(patient))
                .thenReturn(new PatientDtoBySarv(101, "John", "Addr", "Phone", 123, 456));

        List<PatientDtoBySarv> result = appointmentService.getPatientsByPrepNurseOnDate(10, Date.valueOf("2025-12-02"));
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    @DisplayName("getAppointmentsByStartDate() should return mapped AppointmentDtoBySarv list")
    void testGetAppointmentsByStartDate() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);

        when(appointmentRepository.findByStartDate(Date.valueOf("2025-12-02"))).thenReturn(List.of(appointment));
        when(entityMapper.toAppointmentDto(appointment))
                .thenReturn(new AppointmentDtoBySarv(1, "2025-12-02", "2025-12-02", 101, "John", 10, "Alice", "Exam", 301, "Dr. Smith"));

        List<AppointmentDtoBySarv> result = appointmentService.getAppointmentsByStartDate(Date.valueOf("2025-12-02"));
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getPatientName());
    }
}
