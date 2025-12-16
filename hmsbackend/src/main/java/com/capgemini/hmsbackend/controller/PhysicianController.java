package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.PhysicianCreateDTO;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.exception.EmployeeIdNotFoundException;
import com.capgemini.hmsbackend.exception.NameNotFoundException;
import com.capgemini.hmsbackend.service.PhysicianServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/physician")
public class PhysicianController {
    @Autowired
    private PhysicianServiceImpl physicianService;

    @GetMapping
    public ResponseEntity<List<PhysicianDTO>> getAllNurses() {
        List<PhysicianDTO> physician = physicianService.getAllPhysician();
        return ResponseEntity.ok(physician);
    }

    @GetMapping("/empid/{empid}")
    public ResponseEntity<PhysicianDTO> getPhysicianByEmployeeId(@PathVariable Integer empid) {
        Physician physician = physicianService.getPhysicianByEmployeeId(empid);
        if (physician == null) {
            throw new EmployeeIdNotFoundException("Physician with EmployeeID " + empid + " not found");
        }

        PhysicianDTO dto = new PhysicianDTO(
                physician.getEmployeeId(),
                physician.getName(),
                physician.getPosition(),
                physician.getSsn()
        );

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<List<PhysicianDTO>> getPhysiciansByName(@PathVariable String name) {
        List<Physician> physicians = physicianService.findByNameIgnoreCase(name);


        if (physicians.isEmpty()) {
            throw new NameNotFoundException("No physicians found with name: " + name);
        }


        List<PhysicianDTO> dtoList = physicians.stream()
                .map(p -> new PhysicianDTO(
                        p.getEmployeeId(),
                        p.getName(),
                        p.getPosition(),
                        p.getSsn()
                ))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/update/ssn/{empid}")
    public ResponseEntity<PhysicianDTO> updatePhysicianSSN(
            @PathVariable Integer empid,
            @RequestBody Map<String, Integer> requestBody) {

        Integer ssn = requestBody.get("ssn");
        if (ssn == null) {
            return ResponseEntity.badRequest().build();
        }

        PhysicianDTO updatedPhysician = physicianService.updatePhysicianSSN(empid, ssn);
        return ResponseEntity.ok(updatedPhysician);
    }

    @GetMapping("/position/{pos}")
    public ResponseEntity<?> getPhysiciansByPosition(@PathVariable String pos) {
        if (pos == null || pos.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid Request",
                    "message", "Position must not be blank."
            ));
        }

        List<PhysicianDTO> physicians = physicianService.findByPosition(pos.trim());

        if (physicians.isEmpty()) {
            return ResponseEntity.ok(Map.of(

                    "message", "No physicians found for position: " + pos,
                    "error","Not Found"
            ));
        }

        return ResponseEntity.ok(physicians);
    }

    @PostMapping("/post")

    public ResponseEntity<PhysicianDTO> create(@Valid @RequestBody PhysicianCreateDTO dto) {
        PhysicianDTO created = physicianService.createPhysician(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    //code by Arshiya
    @PutMapping("/update/name/{empid}")

    public ResponseEntity<Map<String, Object>> updatePhysicianName(
            @PathVariable Integer empid,
            @RequestBody Map<String, String> requestBody) {

        String newName = requestBody.get("name");
        PhysicianDTO updatedPhysician = physicianService.updatePhysicianName(empid, newName);

        Map<String, Object> response = new HashMap<>();
        // response.put("status", "success");
        //response.put("message", "Physician name updated successfully");
        response.put("physician", updatedPhysician);

        return ResponseEntity.ok(response);
    }

//By Sahithi
@PutMapping("/update/position/{position}/{employeeId}")
public ResponseEntity<String> updatePhysicianPosition(@PathVariable String position,
                                                      @PathVariable Integer employeeId) {
    physicianService.updatePosition(employeeId, position);
    return ResponseEntity.ok("Position updated successfully for Employee ID: " + employeeId);
}





}
