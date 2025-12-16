package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.PrescribesDto;
import com.capgemini.hmsbackend.entity.Prescribes;
import com.capgemini.hmsbackend.entity.PrescribesId;
import com.capgemini.hmsbackend.entity.Appointment;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Medication;
import com.capgemini.hmsbackend.exception.ResourceNotFoundException;
import com.capgemini.hmsbackend.repository.PrescribesRepository;
import com.capgemini.hmsbackend.repository.PhysicianRepository;
import com.capgemini.hmsbackend.repository.PatientRepository;
import com.capgemini.hmsbackend.repository.MedicationRepository;
import com.capgemini.hmsbackend.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrescribesServiceImpl implements IPrescribesService {

    @Autowired
    private PrescribesRepository prescribesRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public String addPrescribes(PrescribesDto dto) {
        // verify referenced entities
        Physician physician = physicianRepository.findById(dto.getPhysicianId()).orElseThrow(() -> new ResourceNotFoundException("Physician not found: " + dto.getPhysicianId()));
        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + dto.getPatientId()));
        Medication medication = medicationRepository.findById(dto.getMedicationId()).orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + dto.getMedicationId()));

        Appointment appointment = null;
        if (dto.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(dto.getAppointmentId()).orElse(null);
        }

        Prescribes p = new Prescribes();
        PrescribesId id = new PrescribesId(dto.getPhysicianId(), dto.getPatientId(), dto.getMedicationId(), dto.getDate());
        p.setId(id);
        p.setPhysician(physician);
        p.setPatient(patient);
        p.setMedication(medication);
        p.setAppointment(appointment);
        p.setDose(dto.getDose());

        prescribesRepository.save(p);
        return "Prescription created";
    }

    @Override
    public List<PrescribesDto> getAllPrescriptions() {
        List<Prescribes> list = prescribesRepository.findAll();
        return list.stream().map(pr -> new PrescribesDto(
                pr.getId().getPhysicianId(),
                pr.getId().getPatientId(),
                pr.getId().getMedicationId(),
                pr.getId().getDate(),
                pr.getAppointment() != null ? pr.getAppointment().getAppointmentId() : null,
                pr.getDose(),
                // physicianName
                pr.getPhysician() != null ? pr.getPhysician().getName() : null,
                // medicationName
                pr.getMedication() != null ? pr.getMedication().getName() : null
        )).toList();
    }

    @Override
    public Optional<PrescribesDto> getPrescriptionById(Integer physicianId, Integer patientId, Integer medicationId, LocalDateTime date) {
        PrescribesId id = new PrescribesId(physicianId, patientId, medicationId, date);
        return prescribesRepository.findById(id).map(pr -> new PrescribesDto(
                pr.getId().getPhysicianId(),
                pr.getId().getPatientId(),
                pr.getId().getMedicationId(),
                pr.getId().getDate(),
                pr.getAppointment() != null ? pr.getAppointment().getAppointmentId() : null,
                pr.getDose(),
                pr.getPhysician() != null ? pr.getPhysician().getName() : null,
                pr.getMedication() != null ? pr.getMedication().getName() : null
        ));
    }

    @Override
    public PrescribesDto updatePrescription(Integer physicianId, Integer patientId, Integer medicationId, LocalDateTime date, PrescribesDto dto) {
        PrescribesId id = new PrescribesId(physicianId, patientId, medicationId, date);
        Prescribes pr = prescribesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        if (dto.getDose() != null) pr.setDose(dto.getDose());
        if (dto.getAppointmentId() != null) {
            Appointment app = appointmentRepository.findById(dto.getAppointmentId()).orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + dto.getAppointmentId()));
            pr.setAppointment(app);
        }

        Prescribes saved = prescribesRepository.save(pr);
        return new PrescribesDto(
                saved.getId().getPhysicianId(),
                saved.getId().getPatientId(),
                saved.getId().getMedicationId(),
                saved.getId().getDate(),
                saved.getAppointment() != null ? saved.getAppointment().getAppointmentId() : null,
                saved.getDose(),
                saved.getPhysician() != null ? saved.getPhysician().getName() : null,
                saved.getMedication() != null ? saved.getMedication().getName() : null
        );
    }

    // new: return all prescriptions for a patient SSN
    @Override
    public List<PrescribesDto> getPrescriptionsForPatient(Integer patientSsn) {
        List<Prescribes> list = prescribesRepository.findByPatientSsn(patientSsn);
        return list.stream().map(pr -> new PrescribesDto(
                pr.getId().getPhysicianId(),
                pr.getId().getPatientId(),
                pr.getId().getMedicationId(),
                pr.getId().getDate(),
                pr.getAppointment() != null ? pr.getAppointment().getAppointmentId() : null,
                pr.getDose(),
                pr.getPhysician() != null ? pr.getPhysician().getName() : null,
                pr.getMedication() != null ? pr.getMedication().getName() : null
        )).toList();
    }
}
