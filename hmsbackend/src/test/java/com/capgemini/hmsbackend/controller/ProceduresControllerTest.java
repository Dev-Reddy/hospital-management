
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.service.IProceduresService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProceduresController.class)
class ProceduresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProceduresService proceduresService;


    @Test
    void testGetCostById() throws Exception {
        ProceduresDto procedure = new ProceduresDto(101, "Heart Surgery", 50000.0);
        Mockito.when(proceduresService.getCostById(101)).thenReturn(procedure);

        mockMvc.perform(get("/api/procedure/cost/{id}", 101)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(101))
                .andExpect(jsonPath("$.name").value("Heart Surgery"))
                .andExpect(jsonPath("$.cost").value(50000.0));
    }

}
