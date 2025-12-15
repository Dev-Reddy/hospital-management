package com.training.repo;

import com.training.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
}