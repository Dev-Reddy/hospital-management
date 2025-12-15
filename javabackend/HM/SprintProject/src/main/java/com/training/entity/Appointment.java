package com.training.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @Column(name = "AppointmentID")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "Patient")   
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "Physician") 
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "PrepNurse") 
    private Nurse prepNurse;

    @Column(name = "Starto")        
    private LocalDateTime start;

    @Column(name = "Endo")         
    private LocalDateTime endTime;

    @Column(name = "ExaminationRoom")
    private String examinationRoom;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    public Nurse getPrepNurse() {
        return prepNurse;
    }

    public void setPrepNurse(Nurse prepNurse) {
        this.prepNurse = prepNurse;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getExaminationRoom() {
        return examinationRoom;
    }

    public void setExaminationRoom(String examinationRoom) {
        this.examinationRoom = examinationRoom;
    }
}