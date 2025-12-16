
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.service.PhysicianServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhysicianController.class)
class PhysicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhysicianServiceImpl physicianService;

    @Test
    void testGetPhysicianByEmployeeId() throws Exception {
        Physician physician = new Physician(101, "Dr. Smith", "Cardiologist", 12345, null, null, null, null, null);
        Mockito.when(physicianService.getPhysicianByEmployeeId(101)).thenReturn(physician);

        mockMvc.perform(get("/api/physician/empid/{empid}", 101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(101))
                .andExpect(jsonPath("$.name").value("Dr. Smith"))
                .andExpect(jsonPath("$.position").value("Cardiologist"))
                .andExpect(jsonPath("$.ssn").value(12345));
    }

    @Test
    void testGetPhysiciansByName() throws Exception {
        List<Physician> physicians = Arrays.asList(
                new Physician(101, "Dr. Smith", "Cardiologist", 12345, null, null, null, null, null),
                new Physician(102, "Dr. Smith", "Neurologist", 67890, null, null, null, null, null)
        );
        Mockito.when(physicianService.findByNameIgnoreCase("Dr. Smith")).thenReturn(physicians);

        mockMvc.perform(get("/api/physician/name/{name}", "Dr. Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeId").value(101))
                .andExpect(jsonPath("$[0].position").value("Cardiologist"))
                .andExpect(jsonPath("$[1].employeeId").value(102))
                .andExpect(jsonPath("$[1].position").value("Neurologist"));
    }

    @Test
    void testUpdatePhysicianSSN() throws Exception {
        PhysicianDTO updated = new PhysicianDTO(101, "Dr. Smith", "Cardiologist", 99999);
        Mockito.when(physicianService.updatePhysicianSSN(101, 99999)).thenReturn(updated);

        String requestBody = "{ \"ssn\": 99999 }";

        mockMvc.perform(put("/api/physician/update/ssn/{empid}", 101)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(101))
                .andExpect(jsonPath("$.ssn").value(99999));
    }

    @Test
    void testGetPhysiciansByPosition_Success() throws Exception {
        List<PhysicianDTO> physicians = Arrays.asList(
                new PhysicianDTO(101, "Dr. Smith", "Cardiologist", 12345)
        );
        Mockito.when(physicianService.findByPosition("Cardiologist")).thenReturn(physicians);

        mockMvc.perform(get("/api/physician/position/{pos}", "Cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].position").value("Cardiologist"));
    }

    @Test
    void testGetPhysiciansByPosition_NotFound() throws Exception {
        Mockito.when(physicianService.findByPosition("Surgeon")).thenReturn(List.of());

        mockMvc.perform(get("/api/physician/position/{pos}", "Surgeon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No physicians found for position: Surgeon"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
