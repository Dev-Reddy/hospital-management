package com.training.service;

import com.training.entity.Physician;
import java.util.List;

public interface PhysicianService {

    Physician getPhysicianById(Integer id);

    List<Physician> getAllPhysicians();
}