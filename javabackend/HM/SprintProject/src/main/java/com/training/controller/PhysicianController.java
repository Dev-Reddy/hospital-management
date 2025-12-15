package com.training.controller;

import com.training.entity.Physician;
import com.training.service.PhysicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/physicians")
public class PhysicianController {

    @Autowired
    private PhysicianService physicianService;

    
    @GetMapping
    public List<Physician> getAllPhysicians() {
        return physicianService.getAllPhysicians();
    }

    @GetMapping("/{id}")
    public Physician getPhysician(@PathVariable("id") Integer id) {
        return physicianService.getPhysicianById(id);
    }
}
