package com.capgemini.hmsbackend.controller;
import com.capgemini.hmsbackend.dto.ProceduresCreateDto;
import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.entity.Procedures;
import com.capgemini.hmsbackend.service.IProceduresService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/procedure")
public class ProceduresController {

    @Autowired
    IProceduresService proceduresService;

    @GetMapping("/cost/id/{id}")
    public ResponseEntity<ProceduresDto> getCost(@PathVariable("id") int code) {
        ProceduresDto procedure = proceduresService.getCostById(code);
        return new ResponseEntity<>(procedure, HttpStatus.OK);
    }
//code by Arshiya
@PutMapping("/cost/{code}")
public ResponseEntity<ProceduresDto> updateProcedureCost(
        @PathVariable Integer code,
        @RequestParam Double cost) {
    ProceduresDto updatedProcedure = proceduresService.updateCost(code, cost);
    return ResponseEntity.ok(updatedProcedure);
}

    @PostMapping
    public ResponseEntity <Map<String, String>> addProcedure(@Valid @RequestBody ProceduresCreateDto dto) {
        String message = proceduresService.addProcedure(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", message));
    }

    @GetMapping
    public ResponseEntity<List<ProceduresDto>> listOfProceduresOfAvailableTreatment() {
        return ResponseEntity.ok(proceduresService.getAllProcedures());
    }



    @PutMapping("/name/{code}")
    public ResponseEntity<ProceduresDto> updateProcedureName(
            @PathVariable Integer code,
            @RequestBody String name) {
        ProceduresDto updatedProcedure = proceduresService.updateName(code, name);
        return ResponseEntity.ok(updatedProcedure);
    }
    @GetMapping("/cost/{name}")
    public ResponseEntity<ProceduresDto> getCostByName(@PathVariable("name") String name) {
        ProceduresDto procedure = proceduresService.getCostByName(name);
        return new ResponseEntity<>(procedure, HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Map<String,String>> deleteProcedure(@PathVariable Integer code) {
        String msg = proceduresService.deleteProcedureByCode(code);
        return ResponseEntity.ok(Map.of("message", msg));
    }
}
