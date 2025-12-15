package com.training.controller;

import com.training.entity.Department;
import com.training.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //  Get all departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    //  Get department by ID
    @GetMapping("/{id}")
    public Department getDepartment(@PathVariable("id") Integer id) {
        return departmentService.getDepartmentById(id);
    }
}