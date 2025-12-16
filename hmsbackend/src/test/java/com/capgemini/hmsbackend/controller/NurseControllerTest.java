
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.NurseDtoBySarv;
import com.capgemini.hmsbackend.exception.NurseNotFoundException;
import com.capgemini.hmsbackend.service.INurseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NurseController.class)
public class  NurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private INurseService nurseService;

    @Test
    @DisplayName("GET /api/nurse/registered/{empid} → returns true")
    void isNurseRegistered_ok() throws Exception {
        when(nurseService.isRegistered(101)).thenReturn(true);

        mockMvc.perform(get("/api/nurse/registered/{empid}", 101))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("GET /api/nurse/{empid} → returns NurseDTO")
    void getNurseDetails_ok() throws Exception {
        NurseDTO dto = new NurseDTO(102, "Alice", "Head Nurse", false);
        when(nurseService.getNurseDTOById(102)).thenReturn(dto);

        mockMvc.perform(get("/api/nurse/{empid}", 102))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(102))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.position").value("Head Nurse"))
                .andExpect(jsonPath("$.registered").value(false));
    }

    @Test
    @DisplayName("GET /api/nurse/position/{empid} → returns position string")
    void getNursePosition_ok() throws Exception {
        when(nurseService.getPositionById(103)).thenReturn("Senior Nurse");

        mockMvc.perform(get("/api/nurse/position/{empid}", 103))
                .andExpect(status().isOk())
                .andExpect(content().string("Senior Nurse"));
    }

    @Test
    @DisplayName("GET /api/nurse → returns list of NurseDTO")
    void getAllNurses_ok() throws Exception {
        when(nurseService.getAllNurses()).thenReturn(List.of(
                new NurseDTO(1, "N1", "Staff Nurse", true),
                new NurseDTO(2, "N2", "Head Nurse", false)
        ));

        mockMvc.perform(get("/api/nurse"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("N1"))
                .andExpect(jsonPath("$[1].name").value("N2"));
    }

    @Test
    @DisplayName("PUT /api/nurse/registered/{empid} → updates registered status")
    void updateRegisteredStatus_ok() throws Exception {
        NurseDTO updated = new NurseDTO(104, "N4", "Staff Nurse", true);
        when(nurseService.updateRegisteredStatus(104, true)).thenReturn(updated);

        String body = objectMapper.writeValueAsString(Map.of("registered", true));

        mockMvc.perform(put("/api/nurse/registered/{empid}", 104)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(104))
                .andExpect(jsonPath("$.registered").value(true));
    }

    @Test
    @DisplayName("PUT /api/nurse/registered/{empid} → 400 when missing 'registered'")
    void updateRegisteredStatus_badRequest() throws Exception {
        String badBody = objectMapper.writeValueAsString(Map.of());

        mockMvc.perform(put("/api/nurse/registered/{empid}", 105)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/nurse → creates nurse")
    void createNurse_ok() throws Exception {
        NurseDtoBySarv input = new NurseDtoBySarv(null, "New Nurse", "Junior", true, 444);
        NurseDtoBySarv created = new NurseDtoBySarv(500, "New Nurse", "Junior", true, 444);
        when(nurseService.createNurse(any(NurseDtoBySarv.class))).thenReturn(created);

        mockMvc.perform(post("/api/nurse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(500))
                .andExpect(jsonPath("$.name").value("New Nurse"));
    }

    @Test
    @DisplayName("GET /api/nurse/{empid} → 404 when NurseNotFoundException thrown")
    void getNurseDetails_notFound() throws Exception {
        when(nurseService.getNurseDTOById(999))
                .thenThrow(new NurseNotFoundException("Nurse not found with ID: 999"));

        mockMvc.perform(get("/api/nurse/{empid}", 999))
                .andExpect(status().isNotFound());
    }
}
