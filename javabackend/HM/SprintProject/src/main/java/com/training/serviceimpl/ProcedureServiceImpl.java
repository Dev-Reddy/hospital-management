package com.training.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.entity.Procedure;
import com.training.exception.ResourceNotFoundException;
import com.training.repo.ProcedureRepository;
import com.training.service.ProcedureService;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    private final ProcedureRepository procedureRepository;

    public ProcedureServiceImpl(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public Procedure saveProcedure(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    @Override
    public Procedure getProcedureById(Integer code) {
        return procedureRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Procedure", "code", code));
    }

    @Override
    public List<Procedure> getAllProcedures() {
        return procedureRepository.findAll();
    }

    @Override
    public Procedure updateProcedure(Integer code, Procedure updatedProcedure) {
        Procedure existing = procedureRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Procedure", "code", code));
        existing.setName(updatedProcedure.getName());
        existing.setCost(updatedProcedure.getCost());
        return procedureRepository.save(existing);
    }

    @Override
    public void deleteProcedure(Integer code) {
        if (!procedureRepository.existsById(code)) {
            throw new ResourceNotFoundException("Procedure", "code", code);
        }
        procedureRepository.deleteById(code);
    }
}
