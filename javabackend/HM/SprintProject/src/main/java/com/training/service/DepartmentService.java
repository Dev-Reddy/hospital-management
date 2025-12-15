package com.training.service;

import com.training.entity.Department;
import java.util.List;

public interface DepartmentService {

    Department getDepartmentById(Integer id);

    List<Department> getAllDepartments();
}