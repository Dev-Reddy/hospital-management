
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.service.IAffiliatedWithService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AffiliatedWithController.class)
class AffiliatedWithControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAffiliatedWithService affiliatedWithService;

    @Test
    void testUpdatePrimaryAffiliation() throws Exception {
        Integer physicianId = 101;
        Mockito.when(affiliatedWithService.updatePrimaryAffiliation(physicianId)).thenReturn(true);

        mockMvc.perform(get("/api/affiliated_with/primary/{physicianid}", physicianId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


    @Test
    void testGetDepartmentsByPhysician() throws Exception {
        Integer physicianId = 101;
        List<DepartmentDTO> departments = Arrays.asList(
                new DepartmentDTO( physicianId, "Cardiology"),
                new DepartmentDTO(physicianId, "Neurology")
        );

        Mockito.when(affiliatedWithService.getDepartmentsByPhysician(physicianId)).thenReturn(departments);


        mockMvc.perform(get("/api/affiliated_with/department/{physicianid}", physicianId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].departmentId").value(1))
                .andExpect(jsonPath("$[0].name").value("Cardiology"))
                .andExpect(jsonPath("$[1].departmentId").value(2))
                .andExpect(jsonPath("$[1].name").value("Neurology"));

    }

}
