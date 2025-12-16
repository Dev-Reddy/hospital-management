package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.SearchItemDto;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.repository.NurseRepository;
import com.capgemini.hmsbackend.repository.PatientRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PatientRepository patientRepository;
    private final PhysicianRepository physicianRepository;
    private final NurseRepository nurseRepository;


    public List<SearchItemDto> searchPatients(String nameOrId, int limit) {
        // parse the integer id (ssn) if possible; otherwise leave null
        Integer id = null;
        try {
            id = Integer.valueOf(nameOrId);
        } catch (NumberFormatException ignored) {
            // not a number; search only by name
        }

        List<Patient> results = patientRepository.searchByNameOrExactId(nameOrId, id);
        return results.stream()
                .limit(sanitizeLimit(limit))
                .map(p -> SearchItemDto.builder()
                        .id(p.getSsn())
                        .name(p.getName())      // single name column
                        .fullName(p.getName())  // mirror for frontend convenience
                        .build())
                .toList();
    }

    public List<SearchItemDto> searchPhysicians(String name, int limit) {
        List<Physician> results = physicianRepository.searchByNameOrSpeciality(name);
        return results.stream()
                .limit(sanitizeLimit(limit))
                .map(ph -> SearchItemDto.builder()
                        .id(ph.getEmployeeId())
                        .name(ph.getName())      // physician name
                        .fullName(ph.getName())  // mirror
                        .build())
                .toList();
    }
    public List<SearchItemDto> searchNurse(String name, int limit) {
        List<Nurse> results = nurseRepository.searchNurseByName(name);

        return results.stream()
                .limit(sanitizeLimit(limit))
                .map(ph -> SearchItemDto.builder()
                        .id(ph.getEmployeeId())
                        .name(ph.getName())      // physician name
                        .fullName(ph.getName())  // mirror
                        .build())
                .toList();
    }


    private int sanitizeLimit(int limit) {
        if (limit <= 0) return 10;
        // cap to avoid large responses
        return Math.min(limit, 50);
    }
}
