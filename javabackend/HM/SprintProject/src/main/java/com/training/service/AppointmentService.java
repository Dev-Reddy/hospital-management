package com.training.service;

import java.util.List;
import com.training.entity.Appointment;

public interface AppointmentService {

    Appointment saveAppointment(Appointment appointment);

    Appointment getAppointmentById(Integer id);

    List<Appointment> getAllAppointments();

    void deleteAppointment(Integer id);
}