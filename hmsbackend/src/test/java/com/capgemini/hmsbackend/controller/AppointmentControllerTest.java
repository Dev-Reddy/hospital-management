
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.*;
import com.capgemini.hmsbackend.service.IAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAppointmentService appointmentService;

    @Test
    @DisplayName("GET /api/appointment → returns list of appointments")
    void getAppointments_ok() throws Exception {
        AppointmentDto a1 = AppointmentDto.builder()
                .appointmentId(1)
                .starto(LocalDateTime.of(2025, 12, 2, 10, 0))
                .endo(LocalDateTime.of(2025, 12, 2, 11, 0))
                .patientId(101)
                .patientName("John Doe")
                .nurseId(201)
                .nurseName("Alice")
                .physicianId(301)
                .physicianName("Dr. Smith")
                .examinationRoom("Room A")
                .build();

        AppointmentDto a2 = AppointmentDto.builder()
                .appointmentId(2)
                .starto(LocalDateTime.of(2025, 12, 3, 9, 0))
                .endo(LocalDateTime.of(2025, 12, 3, 10, 0))
                .patientId(102)
                .patientName("Alice")
                .nurseId(202)
                .nurseName("Bob")
                .physicianId(302)
                .physicianName("Dr. Brown")
                .examinationRoom("Room B")
                .build();

        when(appointmentService.getAllAppointments()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/appointment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].examinationRoom").value("Room A"))
                .andExpect(jsonPath("$[1].examinationRoom").value("Room B"));
    }

    @Test
    @DisplayName("GET /api/appointment → returns message when empty")
    void getAppointments_empty() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(List.of());

        mockMvc.perform(get("/api/appointment"))
                .andExpect(status().isOk())
                .andExpect(content().string("No Appointments Available"));
    }

    @Test
    @DisplayName("POST /api/appointment → creates appointment")
    void createAppointment_ok() throws Exception {
        AppointmentDto dto = AppointmentDto.builder()
                .appointmentId(1)
                .starto(LocalDateTime.of(2025, 12, 2, 10, 0))
                .endo(LocalDateTime.of(2025, 12, 2, 11, 0))
                .patientId(101)
                .patientName("John Doe")
                .nurseId(201)
                .nurseName("Alice")
                .physicianId(301)
                .physicianName("Dr. Smith")
                .examinationRoom("Room A")
                .build();

        String body = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Record Created Successfully"));
    }

    @Test
    @DisplayName("GET /api/appointment/examinationroom/{appointmentId} → returns room details")
    void getExaminationRoomDetails_ok() throws Exception {
        RoomDto roomDto = RoomDto.builder()
                .roomNumber(101)
                .roomType("General")
                .blockFloor(1)
                .blockCode(10)
                .unavailable(false)
                .build();

        when(appointmentService.getExaminationRoomByAppointmentId(1)).thenReturn(roomDto);

        mockMvc.perform(get("/api/appointment/examinationroom/{appointmentId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(101))
                .andExpect(jsonPath("$.roomType").value("General"))
                .andExpect(jsonPath("$.blockFloor").value(1))
                .andExpect(jsonPath("$.blockCode").value(10))
                .andExpect(jsonPath("$.unavailable").value(false));
    }

    @Test
    @DisplayName("GET /api/appointment/patients/{nurseId}/{date} → returns patients for nurse on date")
    void getPatientsByNurseOnDate_ok() throws Exception {
        PatientDtoBySarv p1 = PatientDtoBySarv.builder().ssn(101).name("John").build();
        PatientDtoBySarv p2 = PatientDtoBySarv.builder().ssn(102).name("Alice").build();

        List<PatientDtoBySarv> patients = List.of(p1, p2);

        when(appointmentService.getPatientsByPrepNurseOnDate(10, Date.valueOf("2025-12-02"))).thenReturn(patients);

        mockMvc.perform(get("/api/appointment/patients/{nurseId}/{date}", 10, "2025-12-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/appointment/patients/{nurseId}/{date} → 400 for invalid date")
    void getPatientsByNurseOnDate_badRequest() throws Exception {
        mockMvc.perform(get("/api/appointment/patients/{nurseId}/{date}", 10, "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("date must be in yyyy-MM-dd format"));
    }

    @Test
    @DisplayName("GET /api/appointment/patient/physician/{physicianId}/{patientId} → returns patient if exists")
    void getPatientByPhysicianAndPatient_ok() throws Exception {
        PatientDtoBySarv patient = PatientDtoBySarv.builder().ssn(101).name("John").build();

        when(appointmentService.getPatientByPhysicianAndPatientId(301, 101)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/appointment/patient/physician/{physicianId}/{patientId}", 301, 101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    @DisplayName("GET /api/appointment/patient/physician/{physicianId}/{patientId} → returns 404 if not found")
    void getPatientByPhysicianAndPatient_notFound() throws Exception {
        when(appointmentService.getPatientByPhysicianAndPatientId(301, 999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointment/patient/physician/{physicianId}/{patientId}", 301, 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No record for physician 301 and patient 999"));
    }

    @Test
    @DisplayName("GET /api/appointment/{startdate} → returns appointments by start date")
    void getAppointmentsByStartDate_ok() throws Exception {
        AppointmentDtoBySarv a1 = AppointmentDtoBySarv.builder()
                .appointmentId(1)
                .appointmentStartDate("2025-12-02")
                .appointmentEndDate("2025-12-02")
                .patientId(101)
                .patientName("John")
                .build();

        AppointmentDtoBySarv a2 = AppointmentDtoBySarv.builder()
                .appointmentId(2)
                .appointmentStartDate("2025-12-02")
                .appointmentEndDate("2025-12-02")
                .patientId(102)
                .patientName("Alice")
                .build();

        when(appointmentService.getAppointmentsByStartDate(Date.valueOf("2025-12-02"))).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/appointment/{startdate}", "2025-12-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName").value("John"))
                .andExpect(jsonPath("$[1].patientName").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/appointment/{startdate} → 400 for invalid date")
    void getAppointmentsByStartDate_badRequest() throws Exception {
        mockMvc.perform(get("/api/appointment/{startdate}", "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("startdate must be in yyyy-MM-dd format"));
    }
}
