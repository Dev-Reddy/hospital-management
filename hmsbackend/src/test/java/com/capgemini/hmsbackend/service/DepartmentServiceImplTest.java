
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.entity.Department;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.exception.DepartmentNotFoundException;
import com.capgemini.hmsbackend.exception.EmployeeIdNotFoundException;
import com.capgemini.hmsbackend.repository.DepartmentRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import org.aspectj.apache.bcel.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {


    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDepartmentById_Success() {
        Department dept = new Department();
        dept.setDepartmentId(1);
        dept.setName("Cardiology");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));

        DepartmentDTO result = departmentService.getDepartmentById(1);

        assertEquals(1, result.getDepartmentId());
        assertEquals("Cardiology", result.getName());
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentById(99));
    }

//    @Test
//    void testGetAllDepartments() {
//        Department dept1 = new Department();
//        dept1.setDepartmentId(1);
//        dept1.setName("Cardiology");
//
//        Department dept2 = new Department();
//        dept2.setDepartmentId(2);
//        dept2.setName("Neurology");
//
//        when(departmentRepository.findAll()).thenReturn(Arrays.asList(dept1, dept2));
//
////        List<DepartmentDTO> result = departmentService.getAllDepartments();
//
//        assertEquals(2, result.size());
//        assertEquals("Cardiology", result.get(0).getName());
//        assertEquals("Neurology", result.get(1).getName());
//    }

    @Test
    void testGetDepartmentsByHeadId_Success() {
        Department dept = new Department();
        dept.setDepartmentId(1);
        dept.setName("Cardiology");

        when(departmentRepository.findByHead_EmployeeId(101)).thenReturn(Arrays.asList(dept));

        List<DepartmentDTO> result = departmentService.getDepartmentsByHeadId(101);

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getName());
    }

    @Test
    void testGetDepartmentsByHeadId_NotFound() {
        when(departmentRepository.findByHead_EmployeeId(101)).thenReturn(List.of());

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentsByHeadId(101));
    }

    @Test
    void testIsPhysicianHead() {
        when(departmentRepository.existsByHead_EmployeeId(101)).thenReturn(true);

        assertTrue(departmentService.isPhysicianHead(101));
    }

    @Test
    void testUpdateDepartmentHead_Success() {

        Department dept = new Department();
        dept.setDepartmentId(1);
        dept.setName("Cardiology");

        Physician physician = new Physician();
        physician.setEmployeeId(200);
        physician.setName("Dr. Smith");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));
        when(physicianRepository.findById(200)).thenReturn(Optional.of(physician));
        when(departmentRepository.save(dept)).thenReturn(dept);


        DepartmentDTO result = departmentService.updateDepartmentHead(1, 200);

        assertEquals(1, result.getDepartmentId());
        assertEquals("Cardiology", result.getName());
        verify(departmentRepository, times(1)).save(dept);
    }

    @Test
    void testUpdateDepartmentHead_DepartmentNotFound() {
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.updateDepartmentHead(1, 200));
    }

    @Test
    void testUpdateDepartmentHead_PhysicianNotFound() {
        Department dept = new Department();
        dept.setDepartmentId(1);
        dept.setName("Cardiology");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));
        when(physicianRepository.findById(200)).thenReturn(Optional.empty());

        assertThrows(EmployeeIdNotFoundException.class,
                () -> departmentService.updateDepartmentHead(1, 200));
    }
}
