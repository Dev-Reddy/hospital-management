
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.dto.TrainedInDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ITrainedInService {
    List<ProceduresDto> getTreatmentsByPhysician(Integer physicianId);
    List<PhysicianDTO> getPhysiciansByProcedure(int procedureId);

    boolean updateCertificationExpiry(Integer physicianId, Integer procedureId, LocalDateTime expiryDate);
   // boolean updateCertificationExpiry(Integer physicianId, Integer procedureId, LocalDateTime expiryDate);

    //code by sarvadnya

    List<TrainedInDto> getExpiredSoon(Integer physicianId);

    String addTrainedIn(TrainedInDto dto);
}
