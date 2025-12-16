
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.dto.DepartmentHeadDto;
import com.capgemini.hmsbackend.service.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DepartmentServiceImpl departmentService;

    @Test
    void testGetDepartmentById() throws Exception {
        DepartmentDTO dept = new DepartmentDTO(1, "Cardiology");
        Mockito.when(departmentService.getDepartmentById(1)).thenReturn(dept);

        mockMvc.perform(get("/api/department/{deptid}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(1))
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }
//
//    @Test
//    void testGetAllDepartments() throws Exception {
//        List<DepartmentDTO> departments = Arrays.asList(
//                new DepartmentDTO(1, "Cardiology"),
//                new DepartmentDTO(2, "Neurology")
//        );
//        Mockito.when(departmentService.getAllDepartments()).thenReturn(departments);
//
//        mockMvc.perform(get("/api/department").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].departmentId").value(1))
//                .andExpect(jsonPath("$[0].name").value("Cardiology"))
//                .andExpect(jsonPath("$[1].departmentId").value(2))
//                .andExpect(jsonPath("$[1].name").value("Neurology"));
//    }

    @Test
    void testGetDepartmentsByHead() throws Exception {
        List<DepartmentDTO> departments = Arrays.asList(
                new DepartmentDTO(1, "Cardiology")
        );
        Mockito.when(departmentService.getDepartmentsByHeadId(101)).thenReturn(departments);

        mockMvc.perform(get("/api/department/head/{head}", 101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].departmentId").value(1))
                .andExpect(jsonPath("$[0].name").value("Cardiology"));
    }

    @Test
    void testIsPhysicianHead() throws Exception {
        Mockito.when(departmentService.isPhysicianHead(101)).thenReturn(true);

        mockMvc.perform(get("/api/department/check/{physicianid}", 101))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testUpdateHead() throws Exception {
        DepartmentDTO updatedDept = new DepartmentDTO(1, "Cardiology");
        Mockito.when(departmentService.updateDepartmentHead(1, 200)).thenReturn(updatedDept);

        String requestBody = "{ \"headId\": 200 }";

        mockMvc.perform(put("/api/department/update/headid/{deptid}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(1))
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }
}
