
package com.capgemini.hmsbackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.PatientDtoBySarv;
import com.capgemini.hmsbackend.service.IPatientService;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPatientService patientService;

//    @Test
//    void testGetPatientCheckedByPhysician() throws Exception {
//        PatientDto patient = new PatientDto(101, "John Doe", "Hyderabad", "9876543210", 123);
//        Mockito.when(patientService.getPatientCheckedByPhysician(1, 101)).thenReturn(patient);
//
//        mockMvc.perform(get("/api/patient/{physicianId}/{patientId}", 1, 101))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.ssn").value(101))
//                .andExpect(jsonPath("$.name").value("John Doe"))
//                .andExpect(jsonPath("$.address").value("Hyderabad"));
//    }

    @Test
    void testGetInsuranceIdByPatientId_Success() throws Exception {
        Mockito.when(patientService.getInsuranceIdByPatientId(101)).thenReturn(Optional.of(123));

        mockMvc.perform(get("/api/patient/insurance/{patientId}", 101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuranceId").value(123));
    }


    @Test
    void testGetInsuranceIdByPatientId_NotFound() throws Exception {
        Mockito.when(patientService.getInsuranceIdByPatientId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patient/insurance/{patientId}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Patient Not Found"))
                .andExpect(jsonPath("$.message").value("No patient found for ID: 999"));
    }


    @Test
    void testGetPatientsCheckedByNurse() throws Exception {
        List<PatientDto> patients = Arrays.asList(
                new PatientDto(101, "John Doe", "Hyderabad", "9876543210", 123, null),
                new PatientDto(102, "Jane Doe", "Mumbai", "9876543211", 124, null)
        );
        Mockito.when(patientService.getPatientsCheckedByNurse(10)).thenReturn(patients);

        mockMvc.perform(get("/api/patient/patient/{nurseId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }


    @Test
    void testUpdatePhone() throws Exception {
        PatientDtoBySarv updated = new PatientDtoBySarv();
        updated.setPhone("9876543210");

        Mockito.when(patientService.update(eq(101), any(PatientDtoBySarv.class))).thenReturn(updated);

        String requestBody = "{ \"phone\": \"9876543210\" }";

        mockMvc.perform(put("/api/patient/phone/{ssn}", 101)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("9876543210"));
    }

}