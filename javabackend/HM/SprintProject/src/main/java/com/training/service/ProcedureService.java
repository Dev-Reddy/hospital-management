package com.training.service;

import com.training.entity.Procedure;
import java.util.List;

public interface ProcedureService {

    Procedure saveProcedure(Procedure procedure);

    Procedure getProcedureById(Integer code);

    List<Procedure> getAllProcedures();

    Procedure updateProcedure(Integer code, Procedure procedure);

    void deleteProcedure(Integer code);
}