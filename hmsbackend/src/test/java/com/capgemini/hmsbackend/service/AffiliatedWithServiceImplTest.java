
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.entity.AffiliatedWith;
import com.capgemini.hmsbackend.entity.Department;
import com.capgemini.hmsbackend.exception.EmployeeIdNotFoundException;
import com.capgemini.hmsbackend.repository.AffiliatedWithRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AffiliatedWithServiceImplTest {

    @Mock
    private AffiliatedWithRepository affiliatedWithRepository;

    @InjectMocks
    private AffiliatedWithServiceImpl affiliatedWithService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUpdatePrimaryAffiliation_Success() {
        Integer physicianId = 101;
        AffiliatedWith aff1 = new AffiliatedWith();
        aff1.setPrimaryAffiliation(true);
        AffiliatedWith aff2 = new AffiliatedWith();
        aff2.setPrimaryAffiliation(true);

        List<AffiliatedWith> affiliations = Arrays.asList(aff1, aff2);

        when(affiliatedWithRepository.findByPhysician_EmployeeId(physicianId)).thenReturn(affiliations);

        boolean result = affiliatedWithService.updatePrimaryAffiliation(physicianId);

        assertTrue(result);
        assertTrue(affiliations.get(0).isPrimaryAffiliation());
        assertFalse(affiliations.get(1).isPrimaryAffiliation());
        verify(affiliatedWithRepository, times(1)).saveAll(affiliations);
    }


    @Test
    void testUpdatePrimaryAffiliation_NoAffiliations() {
        Integer physicianId = 999;
        when(affiliatedWithRepository.findByPhysician_EmployeeId(physicianId)).thenReturn(List.of());

        assertThrows(EmployeeIdNotFoundException.class,
                () -> affiliatedWithService.updatePrimaryAffiliation(physicianId));
    }

    @Test
    void testGetDepartmentsByPhysician() {
        Integer physicianId = 101;
        Department dept1 = new Department();
        dept1.setDepartmentId(1);
        dept1.setName("Cardiology");

        Department dept2 = new Department();
        dept2.setDepartmentId(2);
        dept2.setName("Neurology");

        when(affiliatedWithRepository.findDepartmentsByPhysicianId(physicianId))
                .thenReturn(Arrays.asList(dept1, dept2));

        List<DepartmentDTO> result = affiliatedWithService.getDepartmentsByPhysician(physicianId);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals("Cardiology", result.get(0).getName());
        assertEquals(2, result.get(1).getDepartmentId());
        assertEquals("Neurology", result.get(1).getName());
    }
}
