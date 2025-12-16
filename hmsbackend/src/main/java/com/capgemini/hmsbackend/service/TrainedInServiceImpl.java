
package com.capgemini.hmsbackend.service;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.exception.InvalidProcedureException;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.dto.TrainedInDto;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.entity.Procedures;
import com.capgemini.hmsbackend.entity.TrainedIn;
import com.capgemini.hmsbackend.entity.TrainedInId;
import com.capgemini.hmsbackend.exception.PhysicianNotFoundException;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import com.capgemini.hmsbackend.repository.ProceduresRepository;
import com.capgemini.hmsbackend.repository.TrainedInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainedInServiceImpl implements ITrainedInService {
    private final TrainedInRepository trainedInRepository;
    private final PhysicianRepository physicianRepository;
    private final ProceduresRepository proceduresRepository;

    @Override
    public List<ProceduresDto> getTreatmentsByPhysician(Integer physicianId) {
        List<ProceduresDto> procedures = trainedInRepository.findProceduresByPhysicianId(physicianId);
        if (procedures.isEmpty()) {
            throw new PhysicianNotFoundException("Physician with ID " + physicianId + " not found or has no procedures.");
        }
        return procedures;
    }

    @Override
    @Cacheable(value = "physiciansByProcedure", key = "#procedureId")
    public List<PhysicianDTO> getPhysiciansByProcedure(int procedureId) {
        if (procedureId <= 0) {
            throw new InvalidProcedureException("Procedure ID is not valid");
        }

        List<Physician> result = trainedInRepository.findPhysiciansByProcedure(procedureId);

        if (result == null || result.isEmpty()) {
            throw new ResourceNotFoundException("No physicians found for procedure ID: " + procedureId);
        }
      System.out.println("this response is coming directly from database");
        // Convert Entity -> DTO
        return result.stream()
                .map(p -> new PhysicianDTO(p.getEmployeeId(), p.getName(), p.getPosition(), p.getSsn()))
                .toList();
    }


//    @Override

    public boolean updateCertificationExpiry(Integer physicianId, Integer procedureId, LocalDateTime expiryDate) {
        int updatedRows = trainedInRepository.updateCertificationExpiry(physicianId, procedureId, expiryDate);
        return updatedRows > 0;
    }

    //code by sarvadnya

    @Override
    public List<TrainedInDto> getExpiredSoon(Integer physicianId) {
        LocalDateTime threshold = LocalDateTime.now().plus(30, ChronoUnit.DAYS);
        List<TrainedIn> list = trainedInRepository.findExpiredSoonByPhysician(physicianId, threshold);
        return list.stream().map(t -> {
            TrainedInDto dto = new TrainedInDto();
            dto.setPhysicianId(t.getPhysician().getEmployeeId());
            dto.setProcedureCode(t.getProcedure().getCode());
            if (t.getCertificationDate() != null) dto.setCertificationDateIso(t.getCertificationDate().toString());
            if (t.getCertificationExpires() != null) dto.setCertificationExpiresIso(t.getCertificationExpires().toString());
            return dto;
        }).toList();
    }

    @Override
    public String addTrainedIn(TrainedInDto dto) {
        Physician p = physicianRepository.findById(dto.getPhysicianId()).orElseThrow();
        Procedures proc = proceduresRepository.findById(dto.getProcedureCode()).orElseThrow();
        TrainedInId id = new TrainedInId(dto.getPhysicianId(), dto.getProcedureCode());
        TrainedIn t = new TrainedIn();
        t.setId(id);
        t.setPhysician(p);
        t.setProcedure(proc);
        // parse ISO date strings coming from DTO
        try {
            if (dto.getCertificationDateIso() != null) {
                LocalDateTime certDate = LocalDateTime.parse(dto.getCertificationDateIso());
                t.setCertificationDate(certDate);
            }
            if (dto.getCertificationExpiresIso() != null) {
                LocalDateTime expires = LocalDateTime.parse(dto.getCertificationExpiresIso());
                t.setCertificationExpires(expires);
            }
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format for certification dates. Expected ISO-8601, e.g. 2025-10-01T09:00:00", ex);
        }
        trainedInRepository.save(t);
        return "Record Created Successfully";
    }
}


