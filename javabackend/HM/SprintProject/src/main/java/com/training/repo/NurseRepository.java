package com.training.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.Nurse;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    // JpaRepository already gives you CRUD methods
}