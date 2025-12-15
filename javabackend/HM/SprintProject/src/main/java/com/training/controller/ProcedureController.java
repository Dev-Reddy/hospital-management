package com.training.controller;

import com.training.entity.Procedure;
import com.training.service.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    // Get all procedures
    @GetMapping
    public List<Procedure> getAllProcedures() {
        return procedureService.getAllProcedures();
    }

    // Get procedure by code
    @GetMapping("/{code}")
    public Procedure getProcedure(@PathVariable("code") Integer code) {
        return procedureService.getProcedureById(code);
    }
}