package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.NurseDTOBySahithi;
import com.capgemini.hmsbackend.dto.NurseDtoBySarv;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.service.INurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final INurseService nurseService;
    private static final Logger log = LoggerFactory.getLogger(NurseController.class);

    @GetMapping("/registered/{empid}")
    public ResponseEntity<Boolean> isNurseRegistered(@PathVariable Integer empid) {
        boolean registered = nurseService.isRegistered(empid);
        return ResponseEntity.ok(registered);
    }
//
//    @GetMapping("/{empid}")
//    public ResponseEntity<NurseDTO> getNurseDetails(@PathVariable Integer empid) {
//        NurseDTO nurseDTO = nurseService.getNurseDTOById(empid);
//        return ResponseEntity.ok(nurseDTO);
//    }

    @GetMapping("/position/{empid}")
    public ResponseEntity<String> getNursePosition(@PathVariable Integer empid) {
        String position = nurseService.getPositionById(empid);
        return ResponseEntity.ok(position);
    }

    @GetMapping
    public ResponseEntity<List<NurseDTO>> getAllNurses() {
        List<NurseDTO> nurses = nurseService.getAllNurses();
        return ResponseEntity.ok(nurses);
    }

    @PutMapping("/registered/{empid}")
    public ResponseEntity<NurseDTO> updateRegisteredStatus(
            @PathVariable Integer empid,
            @RequestBody Map<String, Boolean> requestBody) {

        Boolean registered = requestBody.get("registered");
        if (registered == null) {
            return ResponseEntity.badRequest().build();
        }

        NurseDTO updatedNurse = nurseService.updateRegisteredStatus(empid, registered);
        return ResponseEntity.ok(updatedNurse);
    }


    //code by sarvadnya

    @PostMapping
    public ResponseEntity<NurseDtoBySarv> createNurse(@RequestBody NurseDtoBySarv dto) {
        NurseDtoBySarv created = nurseService.createNurse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/ssn/{empid}")
    public ResponseEntity<NurseDTOBySahithi> updateSSN(@PathVariable Integer empid, @RequestBody Integer ssn) {
        NurseDTOBySahithi updatedNurse = nurseService.updateSSN(empid, ssn);
        return (updatedNurse != null) ? ResponseEntity.ok(updatedNurse) : ResponseEntity.notFound().build();
    }
    @PutMapping("/{empid}")
    public ResponseEntity<?> updateNurse(@PathVariable Integer empid, @RequestBody(required = false) NurseDtoBySarv dto) {
        log.info("Received updateNurse request for empid={} payload={}", empid, dto);
        if (dto == null) {
            log.warn("updateNurse called with null payload for empid={}", empid);
            return ResponseEntity.badRequest().body(Map.of("error", "Missing request body"));
        }
        try {
            NurseDTOBySahithi updated = nurseService.updateNurse(empid, dto);
            log.info("updateNurse completed for empid={}, result={}", empid, updated);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            log.error("Error updating nurse empid={}: {}", empid, ex.getMessage(), ex);
            throw ex; // let GlobalExceptionHandler handle
        }
    }
    // Additional full-update endpoint: updates all editable nurse fields in one call
    @PutMapping("/update/{empid}")
    public ResponseEntity<?> updateNurseFull(@PathVariable Integer empid, @RequestBody(required = false) NurseDtoBySarv dto) {
        log.info("Received updateNurseFull request for empid={} payload={}", empid, dto);
        if (dto == null) {
            log.warn("updateNurseFull called with null payload for empid={}", empid);
            return ResponseEntity.badRequest().body(Map.of("error", "Missing request body"));
        }
        try {
            NurseDTOBySahithi updated = nurseService.updateNurse(empid, dto);
            log.info("updateNurseFull completed for empid={}, result={}", empid, updated);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            log.error("Error in updateNurseFull empid={}: {}", empid, ex.getMessage(), ex);
            throw ex;
        }
    }
    @GetMapping("/{empid}")
    public ResponseEntity<NurseDTOBySahithi> getNurseDetails(@PathVariable Integer empid) {
        NurseDTOBySahithi nurseDTO = nurseService.getNurseById(empid);
        return ResponseEntity.ok(nurseDTO);
    }

}
