package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.dto.DepartmentDTOBySahithi;
import com.capgemini.hmsbackend.dto.DepartmentHeadDto;
import com.capgemini.hmsbackend.dto.DepartmentUpdateRequest;
import com.capgemini.hmsbackend.service.DepartmentServiceImpl;
import com.capgemini.hmsbackend.service.IDepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentServiceImpl departmentService;

    @Autowired
    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }


    @GetMapping("/{deptid}")
    public DepartmentDTO getDepartmentById(@PathVariable Integer deptid) {
        return departmentService.getDepartmentById(deptid);
    }


    @GetMapping
    public ResponseEntity<List<DepartmentDTOBySahithi>> getAllDepartments() {
        List<DepartmentDTOBySahithi> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/head/{head}")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByHead(@PathVariable("head") Integer head) {
        List<DepartmentDTO> departments = departmentService.getDepartmentsByHeadId(head);
        return ResponseEntity.ok(departments);
    }
    @GetMapping("/check/{physicianid}")
    public ResponseEntity<Boolean> isPhysicianHead(@PathVariable("physicianid") Integer physicianEmployeeId) {
        boolean isHead = departmentService.isPhysicianHead(physicianEmployeeId);
        return ResponseEntity.ok(isHead);
    }




    @PutMapping("/update/deptname/{deptid}")
    public ResponseEntity<Object> updateDeptName(
            @PathVariable("deptid") Integer deptId,
            @Valid @RequestBody DepartmentUpdateRequest request) {

        if (deptId == null || deptId <= 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid Request",
                    "message", "deptid must be a positive integer."
            ));
        }

        DepartmentDTO updated = departmentService.updateDeptName(deptId, request.getDeptName());
        return ResponseEntity.ok(updated);
    }

    //code by sarvadnya

    // GET /api/department/head/{deptid}
    @GetMapping("/headcertification/{deptid}")
    public ResponseEntity<DepartmentDTO> getHeadDetails(@PathVariable Integer deptid) {
        DepartmentDTO dept = departmentService.getDepartmentById(deptid);
        return ResponseEntity.ok(dept);
    }

    // PUT /api/department/update/headid/{deptid}
    @PutMapping("/update/headid/{deptid}")
    public ResponseEntity<DepartmentDTO> updateHead(@PathVariable Integer deptid, @Valid @RequestBody DepartmentHeadDto body) {
        DepartmentDTO updated = departmentService.updateDepartmentHead(deptid, body.getHeadId());
        return ResponseEntity.ok(updated);
    }
    @PostMapping
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentDTOBySahithi departmentDTO) {
        DepartmentDTOBySahithi created = departmentService.createDepartment(departmentDTO);
//
//        Map<String, String> response = new HashMap<>();
        // response.put("message", "Record Created Successfully");
//        response.put("departmentId", String.valueOf(created.getDepartmentId()));
//        response.put("name", created.getName());


        return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
    }

}
