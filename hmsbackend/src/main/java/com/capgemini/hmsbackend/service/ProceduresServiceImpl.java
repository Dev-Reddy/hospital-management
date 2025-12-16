package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.ProceduresCreateDto;
import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.entity.Procedures;
import com.capgemini.hmsbackend.exception.ProceduresNotFoundException;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.ProceduresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProceduresServiceImpl implements  IProceduresService{

    @Autowired
    ProceduresRepository proceduresRepository;

    @Override
    public ProceduresDto getCostById(int code) {
      Procedures procedures =  proceduresRepository.findById(code).orElseThrow(() -> new ProceduresNotFoundException(code));
        return  new ProceduresDto(procedures.getCode(), procedures.getName(), procedures.getCost());
    }
    @Override

    public List<ProceduresDto> getAllProcedures() {

        List<Procedures> res=proceduresRepository.findAll();
        if(res.isEmpty() && res==null){
            throw new ResourceNotFoundException("No treatment available");

        }
        System.out.println("getAllProcedures is called from database");
        return res.stream().map(p->new ProceduresDto(p.getCode(),p.getName(),p.getCost())).toList();


    }


    public ProceduresDto procedureEntitytoDto(Procedures p) {
        ProceduresDto dto = new ProceduresDto();
        dto.setCode(p.getCode());
        dto.setName(p.getName());
        dto.setCost(p.getCost());
        return dto;
    }

    @Override
    public String addProcedure(ProceduresCreateDto dto) {
        Procedures procedure = new Procedures();
//        procedure.setCode(dto.getCode());
        procedure.setName(dto.getName());
        procedure.setCost(dto.getCost());

        proceduresRepository.save(procedure);
        return "Record Created Successfully";
    }



//code by Arshiya
public ProceduresDto updateCost(Integer code, Double cost) {
    Procedures procedure = proceduresRepository.findById(code)
            .orElseThrow(() -> new RuntimeException("Procedure not found with code: " + code));

    procedure.setCost(cost);
    Procedures updatedProcedure = proceduresRepository.save(procedure);

    return new ProceduresDto(updatedProcedure.getCode(),
            updatedProcedure.getName(),
            updatedProcedure.getCost());
}

    public ProceduresDto updateName(Integer code, String name) {
        Procedures procedure = proceduresRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Procedure not found with code: " + code));

        procedure.setName(name);
        Procedures updatedProcedure = proceduresRepository.save(procedure);

        return new ProceduresDto(updatedProcedure.getCode(),
                updatedProcedure.getName(),
                updatedProcedure.getCost());
    }
    @Override
    public ProceduresDto getCostByName(String name) {
        Optional<Procedures> p=  proceduresRepository.findByName(name);
        if(p.isPresent()) {
            Procedures procedure = p.get();
            return new ProceduresDto(procedure.getCode(), procedure.getName(), procedure.getCost());
        }
        else{
            throw new ResourceNotFoundException("Procedure not found: " + name);
        }


    }

    @Override
    public String deleteProcedureByCode(Integer code) {
        if (code == null) throw new IllegalArgumentException("Code required");
        boolean exists = proceduresRepository.existsById(code);
        if (!exists) throw new ResourceNotFoundException("Procedure not found: " + code);
        proceduresRepository.deleteById(code);
        return "Procedure deleted";
    }

}
