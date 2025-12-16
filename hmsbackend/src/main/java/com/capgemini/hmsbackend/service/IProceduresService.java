package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.ProceduresCreateDto;
import com.capgemini.hmsbackend.dto.ProceduresDto;

import java.util.List;

public interface IProceduresService {
    String addProcedure(ProceduresCreateDto dto);
    public ProceduresDto getCostById(int code);
    List<ProceduresDto> getAllProcedures();

    //code by Arshiya
    ProceduresDto updateCost(Integer id , Double cost);
    ProceduresDto updateName(Integer code, String name);
    public ProceduresDto getCostByName(String name);
    // delete procedure by code
    String deleteProcedureByCode(Integer code);
}
