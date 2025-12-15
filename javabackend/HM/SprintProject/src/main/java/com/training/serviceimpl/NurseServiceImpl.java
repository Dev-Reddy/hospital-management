package com.training.serviceimpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.entity.Nurse;
import com.training.exception.ResourceNotFoundException;
import com.training.repo.NurseRepository;
import com.training.service.NurseService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    @Override
    public Nurse saveNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    @Override
    public Nurse getNurseById(Integer id) {
        return nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse", "id", id));
    }

    @Override
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    @Override
    public Nurse updateNurse(Integer id, Nurse nurse) {
        Nurse existing = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse", "id", id));
        existing.setName(nurse.getName());
        existing.setPosition(nurse.getPosition());
        existing.setRegistered(nurse.getRegistered());
        existing.setSsn(nurse.getSsn());
        existing.setUserId(nurse.getUserId());
        existing.setDepartmentId(nurse.getDepartmentId());
        return nurseRepository.save(existing);
    }

    @Override
    public void deleteNurse(Integer id) {
        if (!nurseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nurse", "id", id);
        }
        nurseRepository.deleteById(id);
    }
}
