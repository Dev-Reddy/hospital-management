package com.training.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.entity.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
}