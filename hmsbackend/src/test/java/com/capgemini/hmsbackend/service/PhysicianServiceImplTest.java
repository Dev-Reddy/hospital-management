
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PhysicianCreateDTO;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhysicianServiceImplTest {

    @Mock
    private PhysicianRepository physicianRepository;

    @InjectMocks
    private PhysicianServiceImpl physicianService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPhysicianByEmployeeId() {
        Physician physician = new Physician(101, "Dr. Smith", "Cardiologist", 12345, null, null, null, null, null);
        when(physicianRepository.findByEmployeeId(101)).thenReturn(physician);

        Physician result = physicianService.getPhysicianByEmployeeId(101);

        assertNotNull(result);
        assertEquals(101, result.getEmployeeId());
        assertEquals("Dr. Smith", result.getName());
    }

    @Test
    void testFindByNameIgnoreCase() {
        List<Physician> physicians = Arrays.asList(
                new Physician(101, "Dr. Smith", "Cardiologist", 12345, null, null, null, null, null),
                new Physician(102, "Dr. Smith", "Neurologist", 67890, null, null, null, null, null)
        );
        when(physicianRepository.findByNameIgnoreCase("Dr. Smith")).thenReturn(physicians);

        List<Physician> result = physicianService.findByNameIgnoreCase("Dr. Smith");

        assertEquals(2, result.size());
        assertEquals("Cardiologist", result.get(0).getPosition());
    }

    @Test
    void testFindByPosition() {
        List<Physician> physicians = Arrays.asList(
                new Physician(101, "Dr. Smith", "Cardiologist", 12345, null, null, null, null, null)
        );
        when(physicianRepository.findByPosition("Cardiologist")).thenReturn(physicians);

        List<PhysicianDTO> result = physicianService.findByPosition("Cardiologist");

        assertEquals(1, result.size());
        assertEquals("Cardiologist", result.get(0).getPosition());
    }

    @Test
    void testCreatePhysician_Success() {
        PhysicianCreateDTO request = new PhysicianCreateDTO("Dr. Smith", "Cardiologist", 12345);

        when(physicianRepository.findBySsn(12345)).thenReturn(Optional.empty());
        when(physicianRepository.save(any(Physician.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PhysicianDTO result = physicianService.createPhysician(request);

        assertNotNull(result);
       // assertEquals(101, result.getEmployeeId());
        assertEquals("Dr. Smith", result.getName());
    }

    @Test
    void testCreatePhysician_SsnAlreadyExists() {
        PhysicianCreateDTO request = new PhysicianCreateDTO("Dr. Smith", "Cardiologist", 12345);
        when(physicianRepository.findBySsn(12345)).thenReturn(Optional.of(new Physician()));

        assertThrows(DataIntegrityViolationException.class,
                () -> physicianService.createPhysician(request));
    }

    @Test
    void testUpdatePhysicianSSN_Success() {
        Physician physician = new Physician(101, "Dr. Smith", "Cardiologist", 12345, null, null, null, null, null);
        when(physicianRepository.findById(101)).thenReturn(Optional.of(physician));
        when(physicianRepository.save(physician)).thenReturn(physician);

        PhysicianDTO result = physicianService.updatePhysicianSSN(101, 99999);

        assertEquals(99999, result.getSsn());
    }

    @Test
    void testUpdatePhysicianSSN_NotFound() {
        when(physicianRepository.findById(101)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> physicianService.updatePhysicianSSN(101, 99999));

        assertTrue(exception.getMessage().contains("Physician not found"));
    }
}
