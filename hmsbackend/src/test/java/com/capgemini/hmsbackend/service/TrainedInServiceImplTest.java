
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.dto.TrainedInDto;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.entity.Procedures;
import com.capgemini.hmsbackend.entity.TrainedIn;
import com.capgemini.hmsbackend.exception.PhysicianNotFoundException;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import com.capgemini.hmsbackend.repository.ProceduresRepository;
import com.capgemini.hmsbackend.repository.TrainedInRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TrainedInServiceImplTest {

    @Mock
    private TrainedInRepository trainedInRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @Mock
    private ProceduresRepository proceduresRepository;

    @InjectMocks
    private TrainedInServiceImpl trainedInService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTreatmentsByPhysician_returnsList() {
        List<ProceduresDto> mockProcedures = Arrays.asList(
                new ProceduresDto(101, "Heart Surgery", 50000.0),
                new ProceduresDto(102, "Knee Replacement", 30000.0)
        );
        when(trainedInRepository.findProceduresByPhysicianId(1)).thenReturn(mockProcedures);

        List<ProceduresDto> result = trainedInService.getTreatmentsByPhysician(1);

        assertEquals(2, result.size());
        assertEquals("Heart Surgery", result.get(0).getName());
        verify(trainedInRepository, times(1)).findProceduresByPhysicianId(1);
    }

    @Test
    void getTreatmentsByPhysician_throwsExceptionWhenEmpty() {
        when(trainedInRepository.findProceduresByPhysicianId(1)).thenReturn(List.of());

        assertThrows(PhysicianNotFoundException.class,
                () -> trainedInService.getTreatmentsByPhysician(1));
    }

    @Test
    void updateCertificationExpiry_returnsTrueWhenUpdated() {
        LocalDateTime now = LocalDateTime.now();
        when(trainedInRepository.updateCertificationExpiry(1, 101, now)).thenReturn(1);

        boolean result = trainedInService.updateCertificationExpiry(1, 101, now);

        assertTrue(result);
        verify(trainedInRepository).updateCertificationExpiry(1, 101, now);
    }

    @Test
    void updateCertificationExpiry_returnsFalseWhenNotUpdated() {
        LocalDateTime now = LocalDateTime.now();
        when(trainedInRepository.updateCertificationExpiry(1, 101, now)).thenReturn(0);

        boolean result = trainedInService.updateCertificationExpiry(1, 101, now);

        assertFalse(result);
        verify(trainedInRepository).updateCertificationExpiry(1, 101, now);
    }

    @Test
    void getExpiredSoon_returnsMappedDtoList() {
        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Procedures procedure = new Procedures();
        procedure.setCode(101);

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setPhysician(physician);
        trainedIn.setProcedure(procedure);
        trainedIn.setCertificationDate(LocalDateTime.parse("2025-01-01T00:00:00"));
        trainedIn.setCertificationExpires(LocalDateTime.parse("2025-12-01T00:00:00"));

        when(trainedInRepository.findExpiredSoonByPhysician(eq(1), any(LocalDateTime.class)))
                .thenReturn(List.of(trainedIn));

        List<TrainedInDto> result = trainedInService.getExpiredSoon(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPhysicianId());
        assertEquals(101, result.get(0).getProcedureCode());
        // Matches LocalDateTime.toString() default behavior
        assertEquals("2025-01-01T00:00", result.get(0).getCertificationDateIso());
        assertEquals("2025-12-01T00:00", result.get(0).getCertificationExpiresIso());
        verify(trainedInRepository, times(1)).findExpiredSoonByPhysician(eq(1), any(LocalDateTime.class));
    }

    @Test
    void addTrainedIn_savesEntityAndReturnsMessage() {
        TrainedInDto dto = new TrainedInDto(1, 101, "2025-01-01T00:00:00", "2025-12-01T00:00:00");

        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Procedures procedure = new Procedures();
        procedure.setCode(101);

        when(physicianRepository.findById(1)).thenReturn(Optional.of(physician));
        when(proceduresRepository.findById(101)).thenReturn(Optional.of(procedure));

        String result = trainedInService.addTrainedIn(dto);

        assertEquals("Record Created Successfully", result);
        verify(trainedInRepository, times(1)).save(any(TrainedIn.class));
    }

    @Test
    void addTrainedIn_throwsExceptionForInvalidDate() {
        TrainedInDto dto = new TrainedInDto(1, 101, "invalid-date", "2025-12-01T00:00:00");

        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Procedures procedure = new Procedures();
        procedure.setCode(101);

        when(physicianRepository.findById(1)).thenReturn(Optional.of(physician));
        when(proceduresRepository.findById(101)).thenReturn(Optional.of(procedure));

        assertThrows(IllegalArgumentException.class, () -> trainedInService.addTrainedIn(dto));
        verify(trainedInRepository, never()).save(any(TrainedIn.class));
    }
}
