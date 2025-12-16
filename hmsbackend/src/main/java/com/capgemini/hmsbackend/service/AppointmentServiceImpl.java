
package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.AppointmentDto;
import com.capgemini.hmsbackend.dto.NurseDTO;
import com.capgemini.hmsbackend.dto.PatientDto;
import com.capgemini.hmsbackend.dto.RoomDto;
import com.capgemini.hmsbackend.dto.*;
import com.capgemini.hmsbackend.entity.Appointment;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Room;
import com.capgemini.hmsbackend.exception.AppointmentNotFoundException;
import com.capgemini.hmsbackend.exception.PatientNotFoundForPhysicianException;
import com.capgemini.hmsbackend.exception.PhysicianNotFoundException;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.AppointmentRepository;
import com.capgemini.hmsbackend.repository.PatientRepository;
import com.capgemini.hmsbackend.repository.NurseRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import  com.capgemini.hmsbackend.service.mapper.EntityMapper;

@Service
@RequiredArgsConstructor
public class  AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final NurseRepository nurseRepository;
    private final PhysicianRepository physicianRepository;
    private final PatientServiceImpl patientServiceImpl;
    private final RoomServiceImpl roomServiceImpl;
  
    @Autowired
    private ModelMapper modelMapper;

    private final EntityMapper entityMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(cacheNames = "appointments")
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::appointmentEntityToDto)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "appointmentDates", key = "#patientId")
    public List<LocalDate> getAppointmentDatesByPatient(Integer patientId) {
        return appointmentRepository.findDistinctAppointmentDatesByPatient(patientId)
                .stream()
                .map(date -> date.toLocalDate())
                .toList();
    }
   

//     @Override
//     public String getRoomByAppointmentId(Integer appointmentId) {
//         Appointment appointment = appointmentRepository.findById(appointmentId)
//                 .orElseThrow(() -> new RuntimeException("Appointment not found"));
//         return appointment.getExaminationRoom();
//     }

     private AppointmentDto appointmentEntityToDto(Appointment entity) {
        return  AppointmentDto.builder()
                .appointmentId(entity.getAppointmentId())

                .starto(entity.getStarto().toLocalDate().atTime(entity.getStarto().toLocalTime()))
                .endo(entity.getEndo().toLocalDate().atTime(entity.getEndo().toLocalTime()))
                .patientId(entity.getPatient() != null ? entity.getPatient().getSsn() : null)
                .patientName(entity.getPatient() != null ? entity.getPatient().getName() : null).
                nurseId(entity.getPrepNurse() != null ? entity.getPrepNurse().getEmployeeId() : null)
                .nurseName(entity.getPrepNurse() != null ? entity.getPrepNurse().getName() : null).examinantName(entity.getExaminationRoom())
                .physicianId(entity.getPhysician().getEmployeeId())
                .physicianName(entity.getPhysician().getName())
                .examinationRoom(entity.getExaminationRoom())
                .build();
    }
    @Override
    @Cacheable(cacheNames = "appointmentRoom", key = "#appointmentId")
    public RoomDto getExaminationRoomByAppointmentId(Integer appointmentId) {
        Room room = appointmentRepository.findRoomByAppointmentId(appointmentId);
        if (room == null) {
            throw new AppointmentNotFoundException("No room found for appointment ID: " + appointmentId);
        }
    return roomServiceImpl.roomEntityToDto(room);
    }
   
    @Override
    @Cacheable(cacheNames = "appointmentsByNurse", key = "#nurseId + '-' + #patientId")
    public Optional<PatientDto> getPatientCheckedByNurse(Integer nurseId, Integer patientId) {


        Optional<Patient> patientOpt =
                appointmentRepository.findPatientByNurseAndPatient(nurseId, patientId);

        // Map Patient -> PatientDto only if present; else return Optional.empty()
        return patientOpt.map(patientServiceImpl::patientEntityToDto);

    }

    @CacheEvict(cacheNames = {"appointments", "appointmentDates", "appointmentById"}, allEntries = true)
    @Transactional
    @Override

    public AppointmentDto updateExaminationRoomByAppointmentId(Integer appointmentId, String examinationRoom) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + appointmentId));

        appt.setExaminationRoom(examinationRoom);
        Appointment saved = appointmentRepository.save(appt);


        return appointmentEntityToDto(saved);
    }

    @Override
    @Cacheable(cacheNames = "appointmentRoom", key = "#appointmentId")
    public String getRoomByAppointmentId(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return appointment.getExaminationRoom();
    }



    @Override
    @Cacheable(cacheNames = "nurseDetails", key = "#appointmentId")
    public NurseDTO getNurseDetailsByAppointmentId(Integer appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new EntityNotFoundException("Appointment " + appointmentId + " not found");
        }
        Nurse nurse = appointmentRepository.findPrepNurseByAppointmentId(appointmentId).orElse(null);
        if (nurse == null) {
            return null; // Controller will send 204
        }
        return new NurseDTO(
                nurse.getEmployeeId(),
                nurse.getName(),
                nurse.getPosition(),
                Boolean.TRUE.equals(nurse.isRegistered())

        );
    }



    @Override
    @Cacheable(cacheNames = "physicianByNurseDate", key = "#patientId + '-' + #date")
    public List<AppointmentDto> getPhysicianByNurseOnDate(Integer patientId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatientAndDate(patientId, localDate);

        return appointments.stream()
                .map(appointment -> AppointmentDto.builder()
                        .appointmentId(appointment.getAppointmentId())
                        .starto(appointment.getStarto())
                        .endo(appointment.getEndo())
                        .nurseId(appointment.getPrepNurse() != null ? appointment.getPrepNurse().getEmployeeId() : null)
                        .nurseName(appointment.getPrepNurse() != null ? appointment.getPrepNurse().getName() : null)
                        .physicianId(appointment.getPhysician() != null ? appointment.getPhysician().getEmployeeId() : null)
                        .physicianName(appointment.getPhysician() != null ? appointment.getPhysician().getName() : null)
                        .build()
                )
                .toList();
    }


    @Override
    public List<PatientDto> getPatientsByNurse(Integer nurseId) {
        List<Appointment> appointments = appointmentRepository.findByPrepNurseEmployeeId(nurseId);

        return appointments.stream()
                .map(app -> {
                    if (app.getPatient() != null) {
                        return PatientDto.builder()
                                .ssn(app.getPatient().getSsn())
                                .name(app.getPatient().getName())
                                .address(app.getPatient().getAddress())
                                .phone(app.getPatient().getPhone())
                                .insuranceId(app.getPatient().getInsuranceId())
                                // physicianId removed because PatientDto doesn't have it
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

        @Transactional
    @Override
@CacheEvict(cacheNames = {
        "appointments", "appointmentDates", "appointmentRoom",
        "nurseDetails", "patientsByNurse", "physicianByNurseDate", "roomsByNurseDate"
}, allEntries = true)
    public void createAppointment(AppointmentDto appointmentDto) {
        if (!patientRepository.existsById(appointmentDto.getPatientId())) {
            throw new RuntimeException("Patient not found with ID: " + appointmentDto.getPatientId());
        }

        if (!physicianRepository.existsById(appointmentDto.getPhysicianId())) {
            throw new RuntimeException("Physician not found with ID: " + appointmentDto.getPhysicianId());
        }

        if (appointmentDto.getNurseId() != null && !nurseRepository.existsById(appointmentDto.getNurseId())) {
            throw new RuntimeException("Nurse not found with ID: " + appointmentDto.getNurseId());
        }

        if (appointmentDto.getStarto() == null || appointmentDto.getEndo() == null) {
            throw new RuntimeException("Start and end date-time are required.");
        }
        if (!appointmentDto.getStarto().isBefore(appointmentDto.getEndo())) {
            throw new RuntimeException("Start time must be before end time.");
        }

        Integer customId = generateCustomId();

        entityManager.createNativeQuery(
                        "INSERT INTO appointment (AppointmentID, Patient, PrepNurse, Physician, Starto, Endo, ExaminationRoom) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, customId)
                .setParameter(2, appointmentDto.getPatientId())
                .setParameter(3, appointmentDto.getNurseId())
                .setParameter(4, appointmentDto.getPhysicianId())
                .setParameter(5, appointmentDto.getStarto())
                .setParameter(6, appointmentDto.getEndo())
                .setParameter(7, appointmentDto.getExaminationRoom())
                .executeUpdate();
    }

    private Integer generateCustomId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    @Override
    public List<RoomDto> getRoomsByNurseAndDate(Integer nurseId, LocalDate date) {
        List<Room> rooms = appointmentRepository.findRoomsByNurseAndDate(nurseId, date);

        return rooms.stream()
                .map(r -> new RoomDto(
                        r.getRoomNumber(),
                        r.getRoomType(),
                        r.getBlock().getId().getBlockFloor(),
                        r.getBlock().getId().getBlockCode(),
                        r.isUnavailable()
                ))
                .toList();

    }

//   return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
//   }

   // code by sarvadnya


   @Override
   public List<PatientDtoBySarv> getPatientsByPrepNurseOnDate(int nurseId, java.sql.Date date) {
       List<Patient> patients = appointmentRepository.findPatientsByPrepNurseIdAndDate(nurseId, date);
       return patients.stream().map(entityMapper::toPatientDto).toList();
   }

    @Override
    public List<PatientDtoBySarv> getPatientsByPhysician(int physicianId) {
        List<Patient> patients = appointmentRepository.findPatientsByPhysicianId(physicianId);
        return patients.stream().map(entityMapper::toPatientDto).toList();
    }

    @Override
    public Optional<PatientDtoBySarv> getPatientByPhysicianAndPatientId(Integer physicianId, Integer patientId) {
        return appointmentRepository.findPatientByPhysicianAndPatientId(physicianId, patientId).map(entityMapper::toPatientDto);
    }

    @Override
    public List<AppointmentDtoBySarv> getAppointmentsByStartDate(java.sql.Date startDate) {
        return appointmentRepository.findByStartDate(startDate).stream().map(entityMapper::toAppointmentDto).toList();
    }
    //code by arshiya
    public void addPatient(PatientDto patientDto) {

        Patient patient = new Patient();
        patient.setSsn(patientDto.getSsn());
        patient.setName(patientDto.getName());
        patient.setAddress(patientDto.getAddress());
        patient.setPhone(patientDto.getPhone());
        patient.setInsuranceId(patientDto.getInsuranceId());

        // Fetch Physician using pcpId
        Physician physician = physicianRepository.findById(patientDto.getPcpId())
                .orElseThrow(() -> new RuntimeException("Physician not found with ID: " + patientDto.getPcpId()));
        patient.setPcp(physician);

        patientRepository.save(patient);

    }
    public List<PatientDto> getPatientsByPhysician(Integer physicianId) {
        List<Patient> patients = appointmentRepository.findPatientsByPhysicianId(physicianId);

        if (patients.isEmpty()) {
            throw new PatientNotFoundForPhysicianException("No patients found for Physician ID " + physicianId);
        }

        return patients.stream()
                .map(patient -> PatientDto.builder()
                        .ssn(patient.getSsn())
                        .name(patient.getName())
                        .address(patient.getAddress())
                        .phone(patient.getPhone())
                        .insuranceId(patient.getInsuranceId())
                        .pcpId(patient.getPcp().getEmployeeId())
                        //.pcpName(patient.getPcp().getName())
                        .build())
                .toList();
    }
    public PhysicianDTO getPhysicianByAppointmentId(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        Physician physician = appointment.getPhysician();
        if (physician == null) {
            throw new PhysicianNotFoundException("Physician not found for appointment ID: " + appointmentId);
        }

        return new PhysicianDTO(
                physician.getEmployeeId(),
                physician.getName(),
                physician.getPosition(),
                physician.getSsn()
        );



    }
    @Override
    public RoomDto getRoomByPatientOnDate(Integer patientId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        Room room = appointmentRepository.findRoomByPatientAndDate(patientId, localDate);
        if (room == null) {
            throw new ResourceNotFoundException("No room found for patient ID " + patientId + " on date " + date);
        }
        // Use your RoomServiceImpl for proper mapping of Block and BlockId
        return roomServiceImpl.roomEntityToDto(room);
    }
    @Override
    public PhysicianDTO getPhysiciansByPatientOnDate(Integer patientId, String date) {
        LocalDate localDate = LocalDate.parse(date.trim());
        Physician physicians = appointmentRepository.findPhysiciansByPatientAndDate(patientId, localDate);
        return  new PhysicianDTO(physicians.getEmployeeId(),physicians.getName(),physicians.getPosition(),physicians.getSsn());
    }

    @Override

    public List<PatientDto> getPatientsByPhysicianOnDate(Integer physicianId, String date) {
        LocalDate localDate = LocalDate.parse(date.trim());
        List<Patient> patients = appointmentRepository.findPatientsByPhysicianAndDate(physicianId, localDate);
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("No patients found for physician ID " + physicianId + " on date " + date);
        }
        return patients.stream()
                .map(patient -> PatientDto.builder()
                        .ssn(patient.getSsn())
                        .name(patient.getName())
                        .address(patient.getAddress())
                        .phone(patient.getPhone())

                        .insuranceId(patient.getInsuranceId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatientDto> getPatientByAppointmentId(Integer appointmentId) {
        if (appointmentId == null || appointmentId <= 0) {
            return Optional.empty();
        }

        Optional<Patient> patientOpt = appointmentRepository.findPatientByAppointmentId(appointmentId);

        return patientOpt.map(patientServiceImpl:: patientEntityToDto);
    }


    @Transactional
    public String deleteByAppointmentId(Integer id) {
        boolean exists = appointmentRepository.existsById(id);
        if (!exists) {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
        return "Deleted Appointment with ID: " + id;
    }


}
