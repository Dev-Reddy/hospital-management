
package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.dto.TrainedInDto;
import com.capgemini.hmsbackend.service.ITrainedInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trained_in")
public class TrainedInController {

    private final ITrainedInService trainedInService;



    @GetMapping("/treatment/{physicianId}")
    public List<ProceduresDto> getTreatmentsByPhysician(@PathVariable Integer physicianId) {
        return trainedInService.getTreatmentsByPhysician(physicianId);
    }

    @GetMapping("/physicians/{procedureid}")
    public ResponseEntity<List<PhysicianDTO>> getPhysiciansByProcedure(@PathVariable int procedureid) {
        List<PhysicianDTO> physicians = trainedInService.getPhysiciansByProcedure(procedureid);
        return ResponseEntity.status(HttpStatus.OK).body(physicians);
    }



    @PutMapping("/certificationexpiry/{physicianId}/{procedureId}")
    public ResponseEntity<Boolean> updateCertificationExpiry(
            @PathVariable Integer physicianId,
            @PathVariable Integer procedureId,
            @RequestParam String expiryDate) {

        LocalDateTime parsedDate;
        try {
            parsedDate = LocalDateTime.parse(expiryDate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }

        boolean updated = trainedInService.updateCertificationExpiry(physicianId, procedureId, parsedDate);

        if (updated) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    //code by sarvadnya

    @GetMapping("/expiredsooncerti/{physicianid}")
    public ResponseEntity<List<TrainedInDto>> getExpiredSoon(@PathVariable Integer physicianid) {
        return ResponseEntity.ok(trainedInService.getExpiredSoon(physicianid));
    }

    @PostMapping
    public ResponseEntity<String> addTrainedIn(@RequestBody TrainedInDto dto) {
        return ResponseEntity.ok(trainedInService.addTrainedIn(dto));
    }

}
