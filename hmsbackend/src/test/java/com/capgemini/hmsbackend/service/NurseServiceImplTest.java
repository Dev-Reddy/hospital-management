
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.NurseDtoBySarv;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.exception.NurseNotFoundException;
import com.capgemini.hmsbackend.repository.NurseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class  NurseServiceImplTest {

    @Mock
    private NurseRepository nurseRepository;

    @InjectMocks
    private NurseServiceImpl nurseService;

    @Test
    @DisplayName("isRegistered: returns true when nurse exists")
    void isRegistered_ok() {
        Nurse nurse = new Nurse(101, "Himani", "Staff Nurse", true, 12345, null, null, null);
        when(nurseRepository.findById(101)).thenReturn(Optional.of(nurse));

        boolean result = nurseService.isRegistered(101);

        assertTrue(result);
        verify(nurseRepository).findById(101);
    }

    @Test
    @DisplayName("isRegistered: throws NurseNotFoundException when nurse missing")
    void isRegistered_notFound() {
        when(nurseRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NurseNotFoundException.class, () -> nurseService.isRegistered(999));
        verify(nurseRepository).findById(999);
    }

    @Test
    @DisplayName("getNurseDTOById: returns DTO mapped from entity")
    void getNurseDTOById_ok() {
        Nurse nurse = new Nurse(102, "Alice", "Head Nurse", false, 98765, null, null, null);
        when(nurseRepository.findById(102)).thenReturn(Optional.of(nurse));

        NurseDTO dto = nurseService.getNurseDTOById(102);

        assertEquals(102, dto.getEmployeeId());
        assertEquals("Alice", dto.getName());
        assertEquals("Head Nurse", dto.getPosition());
        assertFalse(dto.isRegistered());
    }

    @Test
    @DisplayName("getPositionById: returns position string")
    void getPositionById_ok() {
        Nurse nurse = new Nurse(103, "Bob", "Senior Nurse", true, 55555, null, null, null);
        when(nurseRepository.findById(103)).thenReturn(Optional.of(nurse));

        String position = nurseService.getPositionById(103);

        assertEquals("Senior Nurse", position);
    }

    @Test
    @DisplayName("getAllNurses: maps entities to DTO list")
    void getAllNurses_ok() {
        Nurse n1 = new Nurse(201, "N1", "Staff Nurse", true, 111, null, null, null);
        Nurse n2 = new Nurse(202, "N2", "Head Nurse", false, 222, null, null, null);
        when(nurseRepository.findAll()).thenReturn(List.of(n1, n2));

        List<NurseDTO> dtos = nurseService.getAllNurses();

        assertEquals(2, dtos.size());
        assertEquals("N1", dtos.get(0).getName());
        assertEquals("N2", dtos.get(1).getName());
    }

    @Test
    @DisplayName("updateRegisteredStatus: updates flag and returns updated DTO")
    void updateRegisteredStatus_ok() {
        Nurse nurse = new Nurse(301, "N3", "Staff Nurse", false, 333, null, null, null);
        when(nurseRepository.findById(301)).thenReturn(Optional.of(nurse));
        when(nurseRepository.save(any(Nurse.class))).thenAnswer(inv -> inv.getArgument(0));

        NurseDTO updated = nurseService.updateRegisteredStatus(301, true);

        assertTrue(updated.isRegistered());
        assertEquals("N3", updated.getName());
        verify(nurseRepository).save(nurse);
    }

    @Test
    @DisplayName("createNurse: saves entity and returns NurseDtoBySarv")
    void createNurse_ok() {
        NurseDtoBySarv input = new NurseDtoBySarv(null, "New Nurse", "Junior", true, 444);
        when(nurseRepository.save(any(Nurse.class))).thenAnswer(inv -> {
            Nurse saved = inv.getArgument(0);
            saved.setEmployeeId(500);
            return saved;
        });

        NurseDtoBySarv result = nurseService.createNurse(input);

        assertEquals(500, result.getEmployeeId());
        assertEquals("New Nurse", result.getName());
        assertEquals("Junior", result.getPosition());
        assertTrue(result.isRegistered());
        assertEquals(444, result.getSsn());
    }
}
