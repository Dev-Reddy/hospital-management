
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.PatientDtoBySarv;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.exception.PatientNotFoundException;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.PatientRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
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

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @InjectMocks
    private PatientServiceImpl patientService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

        @Test
    void testGetPatientCheckedByPhysician_Success() {
        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("John Doe");
        patient.setAddress("Hyderabad");
        patient.setPhone("9876543210");
        patient.setInsuranceId(123);

        when(physicianRepository.existsByEmployeeId(1)).thenReturn(true);
        when(patientRepository.findPatientCheckedByPhysician(1, 101)).thenReturn(Optional.of(patient));

        PatientDto result = patientService.getPatientCheckedByPhysician(1, 101);

        assertEquals(101, result.getSsn());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetPatientCheckedByPhysician_PhysicianNotFound() {
        when(physicianRepository.existsByEmployeeId(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientCheckedByPhysician(1, 101));
    }

    @Test
    void testGetPatientCheckedByPhysician_PatientNotFound() {
        when(physicianRepository.existsByEmployeeId(1)).thenReturn(true);
        when(patientRepository.findPatientCheckedByPhysician(1, 101)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientCheckedByPhysician(1, 101));
    }

    @Test
    void testGetInsuranceIdByPatientId() {
        when(patientRepository.findInsuranceIdByPatientId(101)).thenReturn(Optional.of(123));

        Optional<Integer> result = patientService.getInsuranceIdByPatientId(101);

        assertTrue(result.isPresent());
        assertEquals(123, result.get());
    }

    @Test
    void testUpdatePatientAddressBySsn_Success() {
        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("John Doe");
        patient.setAddress("Old Address");
        patient.setPhone("9876543210");
        patient.setInsuranceId(123);

        when(patientRepository.findBySsn(101)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        PatientDto result = patientService.updatePatientAddressBySsn(101, "New Address");

        assertEquals("New Address", result.getAddress());
    }

    @Test
    void testUpdatePatientAddressBySsn_NotFound() {
        when(patientRepository.findBySsn(101)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> patientService.updatePatientAddressBySsn(101, "New Address"));
    }

    @Test
    void testGetPatientsCheckedByNurse() {
        Patient p1 = new Patient();
        p1.setSsn(101);
        p1.setName("John Doe");

        Patient p2 = new Patient();
        p2.setSsn(102);
        p2.setName("Jane Doe");

        when(patientRepository.findPatientsCheckedByNurse(10)).thenReturn(Arrays.asList(p1, p2));

        List<PatientDto> result = patientService.getPatientsCheckedByNurse(10);

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testUpdatePhone_Success() {
        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("John Doe");
        patient.setPhone("9876543210");

        Physician physician = new Physician();
        physician.setEmployeeId(200);

        PatientDtoBySarv dto = new PatientDtoBySarv();
        dto.setPhone("9999999999");
        dto.setPcpId(200);

        when(patientRepository.findById(101)).thenReturn(Optional.of(patient));
        when(physicianRepository.findById(200)).thenReturn(Optional.of(physician));
        when(patientRepository.save(patient)).thenReturn(patient);

        PatientDtoBySarv result = patientService.update(101, dto);

        assertEquals("9999999999", result.getPhone());
        assertEquals(200, result.getPcpId());
    }

    @Test
    void testUpdatePhone_PatientNotFound() {
        PatientDtoBySarv dto = new PatientDtoBySarv();
        dto.setPhone("9999999999");

        when(patientRepository.findById(101)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.update(101, dto));
    }

    @Test
    void testUpdatePhone_PhysicianNotFound() {
        Patient patient = new Patient();
        patient.setSsn(101);

        PatientDtoBySarv dto = new PatientDtoBySarv();
        dto.setPhone("9999999999");
        dto.setPcpId(200);

        when(patientRepository.findById(101)).thenReturn(Optional.of(patient));
        when(physicianRepository.findById(200)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.update(101, dto));
    }
}
