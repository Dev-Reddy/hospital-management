package com.training.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.entity.Physician;
import com.training.exception.ResourceNotFoundException;
import com.training.repo.PhysicianRepository;
import com.training.service.PhysicianService;

@Service
public class PhysicianServiceImpl implements PhysicianService {

    private final PhysicianRepository physicianRepository;

    public PhysicianServiceImpl(PhysicianRepository physicianRepository) {
        this.physicianRepository = physicianRepository;
    }

    @Override
    public Physician getPhysicianById(Integer id) {
        return physicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Physician", "id", id));
    }

    @Override
    public List<Physician> getAllPhysicians() {
        return physicianRepository.findAll();
    }
}
