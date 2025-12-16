
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.dto.TrainedInDto;
import com.capgemini.hmsbackend.service.ITrainedInService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainedInController.class)
class TrainedInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // for JSON serialization

    @MockBean
    private ITrainedInService trainedInService;

    @Test
    void getTreatmentsByPhysician_returnsList() throws Exception {
        List<ProceduresDto> procedures = Arrays.asList(
                new ProceduresDto(101, "Heart Surgery", 50000.0),
                new ProceduresDto(102, "Knee Replacement", 30000.0)
        );
        Mockito.when(trainedInService.getTreatmentsByPhysician(1)).thenReturn(procedures);

        mockMvc.perform(get("/api/trained_in/treatment/{physicianId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code").value(101))
                .andExpect(jsonPath("$[0].name").value("Heart Surgery"))
                .andExpect(jsonPath("$[0].cost").value(50000.0))
                .andExpect(jsonPath("$[1].code").value(102))
                .andExpect(jsonPath("$[1].name").value("Knee Replacement"))
                .andExpect(jsonPath("$[1].cost").value(30000.0));
    }

    @Test
    void updateCertificationExpiry_success() throws Exception {
        Mockito.when(trainedInService.updateCertificationExpiry(
                1, 101, LocalDateTime.parse("2025-12-31T00:00:00"))
        ).thenReturn(true);

        mockMvc.perform(put("/api/trained_in/certificationexpiry/{physicianId}/{procedureId}", 1, 101)
                        .param("expiryDate", "2025-12-31T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void updateCertificationExpiry_notFound() throws Exception {
        Mockito.when(trainedInService.updateCertificationExpiry(
                1, 999, LocalDateTime.parse("2025-12-31T00:00:00"))
        ).thenReturn(false);

        mockMvc.perform(put("/api/trained_in/certificationexpiry/{physicianId}/{procedureId}", 1, 999)
                        .param("expiryDate", "2025-12-31T00:00:00"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("false"));
    }

    @Test
    void updateCertificationExpiry_invalidDate_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/trained_in/certificationexpiry/{physicianId}/{procedureId}", 1, 101)
                        .param("expiryDate", "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("false"));
    }

    @Test
    void getExpiredSoon_returnsList() throws Exception {
        List<TrainedInDto> expiredSoon = Arrays.asList(
                TrainedInDto.builder()
                        .physicianId(1)
                        .procedureCode(101)
                        .certificationDateIso("2025-01-01T00:00:00")
                        .certificationExpiresIso("2025-12-01T00:00:00")
                        .build(),
                TrainedInDto.builder()
                        .physicianId(1)
                        .procedureCode(102)
                        .certificationDateIso("2025-01-05T00:00:00")
                        .certificationExpiresIso("2025-12-05T00:00:00")
                        .build()
        );
        Mockito.when(trainedInService.getExpiredSoon(1)).thenReturn(expiredSoon);

        mockMvc.perform(get("/api/trained_in/expiredsooncerti/{physicianid}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].physicianId").value(1))
                .andExpect(jsonPath("$[0].procedureCode").value(101))
                .andExpect(jsonPath("$[0].certificationDateIso").value("2025-01-01T00:00:00"))
                .andExpect(jsonPath("$[0].certificationExpiresIso").value("2025-12-01T00:00:00"))
                .andExpect(jsonPath("$[1].physicianId").value(1))
                .andExpect(jsonPath("$[1].procedureCode").value(102))
                .andExpect(jsonPath("$[1].certificationDateIso").value("2025-01-05T00:00:00"))
                .andExpect(jsonPath("$[1].certificationExpiresIso").value("2025-12-05T00:00:00"));
    }

    @Test
    void addTrainedIn_returnsOkWithMessage() throws Exception {
        TrainedInDto dto = TrainedInDto.builder()
                .physicianId(1)
                .procedureCode(101)
                .certificationDateIso("2025-01-01T00:00:00")
                .certificationExpiresIso("2025-12-01T00:00:00")
                .build();

        Mockito.when(trainedInService.addTrainedIn(Mockito.any(TrainedInDto.class)))
                .thenReturn("TrainedIn record added successfully");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/trained_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().string("TrainedIn record added successfully"));
    }
}
