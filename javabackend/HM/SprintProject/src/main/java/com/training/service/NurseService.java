package com.training.service;

import java.util.List;

import com.training.entity.Nurse;

public interface NurseService {
    Nurse saveNurse(Nurse nurse);
    Nurse getNurseById(Integer id);
    List<Nurse> getAllNurses();
    Nurse updateNurse(Integer id, Nurse nurse);
    void deleteNurse(Integer id);
}