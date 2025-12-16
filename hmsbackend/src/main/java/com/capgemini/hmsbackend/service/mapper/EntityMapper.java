package com.capgemini.hmsbackend.service.mapper;

import com.capgemini.hmsbackend.dto.*;
import com.capgemini.hmsbackend.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EntityMapper {

    private final ModelMapper modelMapper;

    public EntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

//    public PhysicianDTO toPhysicianDto(Physician p) {
//        if (p == null) return null;
//        return new PhysicianDTO(p.getEmployeeId(), p.getName(), p.getPosition(), p.getSsn());
//    }
//
//    public NurseDTO toNurseDto(Nurse n) {
//        if (n == null) return null;
//        return new NurseDTO(n.getEmployeeId(), n.getName(), n.getPosition(), n.isRegistered(), n.getSsn());
//    }
//
//    public ProceduresDto toProceduresDto(Procedures p) {
//        if (p == null) return null;
//        ProceduresDto dto = new ProceduresDto();
//        dto.setCode(p.getCode());
//        dto.setName(p.getName());
//        dto.setCost(p.getCost() != null ? p.getCost() : 0.0);
//        return dto;
//    }
//
    public AppointmentDtoBySarv toAppointmentDto(Appointment a) {
        if (a == null) return null;
        AppointmentDtoBySarv dto = AppointmentDtoBySarv.builder()
                .appointmentId(a.getAppointmentId())
                .appointmentStartDate(a.getStarto() != null ? a.getStarto().toString() : null)
                .appointmentEndDate(a.getEndo() != null ? a.getEndo().toString() : null)
                .patientId(a.getPatient() != null ? a.getPatient().getSsn() : null)
                .patientName(a.getPatient() != null ? a.getPatient().getName() : null)
                .nurseId(a.getPrepNurse() != null ? a.getPrepNurse().getEmployeeId() : null)
                .nurseName(a.getPrepNurse() != null ? a.getPrepNurse().getName() : null)
                //.examinantName(a.getExaminationRoom())
                .physicianId(a.getPhysician() != null ? a.getPhysician().getEmployeeId() : null)
                .physicianName(a.getPhysician() != null ? a.getPhysician().getName() : null)
                .build();
        return dto;
    }

    public PatientDtoBySarv toPatientDto(Patient p) {
        if (p == null) return null;
        return PatientDtoBySarv.builder()
                .ssn(p.getSsn())
                .name(p.getName())
                .address(p.getAddress())
                .phone(p.getPhone())
                .insuranceId(p.getInsuranceId())
                .pcpId(p.getPcp() != null ? p.getPcp().getEmployeeId() : null)
                .build();
    }

//    // Map AppointmentDto -> Appointment entity. This creates minimal Patient/Physician/Nurse with only IDs set.
//    public Appointment toAppointmentEntity(AppointmentDto dto) {
//        if (dto == null) return null;
//        Appointment a = new Appointment();
//        // Don't set appointmentId for new entities (JPA will generate) but preserve if provided
//        if(dto.getAppointmentId() != null) {
//            a.setAppointmentId(dto.getAppointmentId());
//        }
//        if (dto.getPatientId() != null) {
//            Patient p = new Patient();
//            p.setSsn(dto.getPatientId());
//            a.setPatient(p);
//        }
//        if (dto.getNurseId() != null) {
//            Nurse n = new Nurse();
//            n.setEmployeeId(dto.getNurseId());
//            a.setPrepNurse(n);
//        }
//        if (dto.getPhysicianId() != null) {
//            Physician ph = new Physician();
//            ph.setEmployeeId(dto.getPhysicianId());
//            a.setPhysician(ph);
//        }
//        if (dto.getAppointmentStartDate() != null) {
//            try {
//                a.setStarto(LocalDateTime.parse(dto.getAppointmentStartDate()));
//            } catch (Exception ignored) {
//            }
//        }
//        if (dto.getAppointmentEndDate() != null) {
//            try {
//                a.setEndo(LocalDateTime.parse(dto.getAppointmentEndDate()));
//            } catch (Exception ignored) {
//            }
//        }
//        a.setExaminationRoom(dto.getExaminantName());
//        return a;
//    }
}
