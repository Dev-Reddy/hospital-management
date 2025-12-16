
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.entity.Procedures;
import com.capgemini.hmsbackend.exception.ProceduresNotFoundException;
import com.capgemini.hmsbackend.repository.ProceduresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProceduresServiceImplTest {

    @Mock
    private ProceduresRepository proceduresRepository;

    @InjectMocks
    private ProceduresServiceImpl proceduresService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCostById_Success() {
        Procedures procedure = new Procedures();
        procedure.setCode(101);
        procedure.setName("Heart Surgery");
        procedure.setCost(50000.0);

        when(proceduresRepository.findById(101)).thenReturn(Optional.of(procedure));

        ProceduresDto result = proceduresService.getCostById(101);

        assertNotNull(result);
        assertEquals(101, result.getCode());
        assertEquals("Heart Surgery", result.getName());
        assertEquals(50000.0, result.getCost());
    }

    @Test
    void testGetCostById_NotFound() {
        when(proceduresRepository.findById(999)).thenReturn(Optional.empty());

        ProceduresNotFoundException exception = assertThrows(ProceduresNotFoundException.class,
                () -> proceduresService.getCostById(999));

        assertTrue(exception.getMessage().contains("999"));
    }
}
