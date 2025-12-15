package com.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.entity.Nurse;
import com.training.service.NurseService;


@RestController
@RequestMapping("/nurses")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PostMapping
    public Nurse createNurse(@RequestBody Nurse nurse) {
        return nurseService.saveNurse(nurse);
    }

    @GetMapping("/{id}")
    public Nurse getNurse(@PathVariable Integer id) {
        return nurseService.getNurseById(id);
    }

    @GetMapping
    public List<Nurse> getAllNurses() {
        return nurseService.getAllNurses();
    }

    @PutMapping("/{id}")
    public Nurse updateNurse(@PathVariable Integer id, @RequestBody Nurse nurse) {
        return nurseService.updateNurse(id, nurse);
    }

    @DeleteMapping("/{id}")
    public String deleteNurse(@PathVariable Integer id) {
        nurseService.deleteNurse(id);
        return "Nurse deleted successfully";
    }
}