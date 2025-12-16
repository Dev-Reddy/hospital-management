package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.*;
import com.capgemini.hmsbackend.entity.Appointment;
import com.capgemini.hmsbackend.entity.Nurse;
import com.capgemini.hmsbackend.entity.Patient;
import com.capgemini.hmsbackend.entity.Room;
import com.capgemini.hmsbackend.service.IAppointmentService;
import com.capgemini.hmsbackend.service.INurseService;
import com.capgemini.hmsbackend.service.IPatientService;
import com.capgemini.hmsbackend.service.NurseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final IAppointmentService appointmentService;
    @Autowired
    private INurseService nurseService;
    @Autowired
    private IPatientService patientService;

    @GetMapping
    public ResponseEntity<Object> getAppointments() {
        List<AppointmentDto>  res=appointmentService.getAllAppointments();
        if(res.isEmpty()){
            return ResponseEntity.ok("No Appointments Available");
        }
        return ResponseEntity.ok(res);
    }
    @GetMapping("/date/{patientId}")
    public ResponseEntity<Object> getAppointmentDates(@PathVariable Integer patientId) {
        List<LocalDate> dates = appointmentService.getAppointmentDatesByPatient(patientId);

        if (dates.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", "Appointment not found for patient ID: " + patientId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(dates);
    }
    @GetMapping("/patient/{nurseId}/{patientId}")
    public ResponseEntity<Object> getPatientByNurseAndPatientId(
            @PathVariable Integer nurseId,
            @PathVariable Integer patientId) {
      if (nurseId == null || nurseId <= 0 || patientId == null || patientId <= 0) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Request");
            errorResponse.put("message", "nurseId and patientId must be positive integers.");
            errorResponse.put("details", Map.of("nurseId", nurseId, "patientId", patientId));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Optional<PatientDto> patient = appointmentService.getPatientCheckedByNurse(nurseId, patientId);


        if (patient.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Patient Not Found");
            errorResponse.put("message", "No patient checked by the specified nurse was found for the given IDs.");
           // errorResponse.put("details", Map.of("nurseId", nurseId, "patientId", patientId));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

            return ResponseEntity.ok(patient.get());

    }

    @GetMapping("/examinationroom/{appointmentId}")
    public ResponseEntity<Object> getExaminationRoomDetails(@PathVariable Integer appointmentId) {
        RoomDto room = appointmentService.getExaminationRoomByAppointmentId(appointmentId);
        return ResponseEntity.ok(room);
    }


    @PutMapping("/room/{appointmentId}")
    public ResponseEntity<Object> updateExaminationRoom(
            @PathVariable Integer appointmentId,
            @RequestBody  String examinationRoom) {
         if(appointmentId == null || appointmentId <= 0 || examinationRoom == null || examinationRoom.isEmpty()){
             return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid Request",
                    "message", "appointment id  must be a positive integer."
            ));
         }

        AppointmentDto updated = appointmentService.updateExaminationRoomByAppointmentId(appointmentId, examinationRoom);

         return ResponseEntity.ok(Map.of("message", "Examination room updated successfully", "updatedexaminationroom", updated.getExaminantName()));
    }
      




    @GetMapping("/nurse/{appointmentId}")
    public ResponseEntity<NurseDTO> getNurseByAppointmentId(@PathVariable Integer appointmentId) {
        try {
            NurseDTO nurseDto = appointmentService.getNurseDetailsByAppointmentId(appointmentId);
            if (nurseDto == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(nurseDto);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




    @GetMapping("/patient/{nurseId}")
    public ResponseEntity<List<PatientDto>> getPatientsByNurse(@PathVariable Integer nurseId) {
        List<PatientDto> patients = appointmentService.getPatientsByNurse(nurseId);
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/nurse/{patientId}/{date}")
    public ResponseEntity<?> getPhysicianByNurseOnDate(@PathVariable Integer patientId,
                                                       @PathVariable String date) {
        List<AppointmentDto> physicians = appointmentService.getPhysicianByNurseOnDate(patientId, date);

        if (physicians.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", "No physician found for patient ID: " + patientId + " on date: " + date);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(physicians);
    }



    @GetMapping("/room/{nurseId}/{date}")
    public ResponseEntity<List<RoomDto>> getRoomsByNurseAndDate(
            @PathVariable Integer nurseId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<RoomDto> rooms = appointmentService.getRoomsByNurseAndDate(nurseId, date);
        return ResponseEntity.ok(rooms);
    }



    @PostMapping
    public ResponseEntity<Map<String, String>> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        appointmentService.createAppointment(appointmentDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Record Created Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

}


    //code by sarvadnya
    // New: get patients checked by a nurse on a particular date
    @GetMapping("/patients/{nurseId}/{date}")
    public ResponseEntity<?> getPatientsByNurseOnDate(@PathVariable int nurseId, @PathVariable String date) {
        try {
            LocalDate ld = LocalDate.parse(date);
            java.sql.Date d = java.sql.Date.valueOf(ld);
            List<PatientDtoBySarv> res = appointmentService.getPatientsByPrepNurseOnDate(nurseId, d);
            return ResponseEntity.ok(res);
        } catch (DateTimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error","Bad Request","message","date must be in yyyy-MM-dd format"));
        }
    }

    @GetMapping("/patient/{physicianId}")
    public ResponseEntity<List<PatientDtoBySarv>> getPatientsByPhysician(@PathVariable int physicianId) {
        List<PatientDtoBySarv> patients = appointmentService.getPatientsByPhysician(physicianId);
        return ResponseEntity.ok(patients);
    }

    // New: get patient by physician and patient id (physician checked that patient)
    @GetMapping("/patient/physician/{physicianId}/{patientId}")
    public ResponseEntity<?> getPatientByPhysicianAndPatient(@PathVariable Integer physicianId, @PathVariable Integer patientId) {
        Optional<PatientDtoBySarv> opt = appointmentService.getPatientByPhysicianAndPatientId(physicianId, patientId);
        if (opt.isPresent()) return ResponseEntity.ok(opt.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Not Found","message","No record for physician " + physicianId + " and patient " + patientId));
    }

    @GetMapping("/{startdate}")
    public ResponseEntity<?> getAppointmentsByStartDate(@PathVariable String startdate) {
        try {
            LocalDate ld = LocalDate.parse(startdate);
            java.sql.Date d = java.sql.Date.valueOf(ld);
            List<AppointmentDtoBySarv> res = appointmentService.getAppointmentsByStartDate(d);
            return ResponseEntity.ok(res);
        } catch (DateTimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error","Bad Request","message","startdate must be in yyyy-MM-dd format"));
        }
    }
    //code by Arshiya
    @GetMapping("/nurse/bypatient/{patientId}")
    public ResponseEntity<List<NurseDTO>> getNursesByPatientId(@PathVariable Integer patientId) {
        List<NurseDTO> nurses = nurseService.getNursesByPatientId(patientId);
        return ResponseEntity.ok(nurses); // Always return 200 with empty list if no nurses
    }
    @GetMapping("/physician/{appointmentId}")
    public ResponseEntity<PhysicianDTO> getPhysicianByAppointmentId(@PathVariable Integer appointmentId) {
        PhysicianDTO physicianDTO = appointmentService.getPhysicianByAppointmentId(appointmentId);
        return ResponseEntity.ok(physicianDTO);
    }
//    @GetMapping("/patient/{physicianid}/{patientid}")
//    public ResponseEntity<PatientDto> getPatientByPhysicianAndPatient(
//            @PathVariable Integer physicianid,
//            @PathVariable Integer patientid) {
//        PatientDto patientDto = patientService.getPatientByPhysicianAndPatient(physicianid, patientid);
//        return ResponseEntity.ok(patientDto);
//    }



    // by sahithi
    @GetMapping("/pateint/byphysician/{physicianId}/patients/{date}")
    public ResponseEntity<List<PatientDto>> getPatientsByPhysicianOnDate(
            @PathVariable Integer physicianId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String dateString = date.toString(); // ✅ Converts LocalDate to "yyyy-MM-dd"
        List<PatientDto> patients = appointmentService.getPatientsByPhysicianOnDate(physicianId, dateString);

        return patients.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(patients);
    }

    @GetMapping("/physician/{patientId}/{date}")
    public ResponseEntity<PhysicianDTO> getPhysiciansByPatientOnDate(
            @PathVariable Integer patientId,
            @PathVariable LocalDate date) {

        PhysicianDTO physicians = appointmentService.getPhysiciansByPatientOnDate(patientId, date.toString().trim());
        return ResponseEntity.ok(physicians);
    }

    @GetMapping("/room/bypatient{patientId}/{date}")
    public ResponseEntity<RoomDto> getRoomByPatientOnDate(
            @PathVariable Integer patientId,
            @PathVariable String date) {

        RoomDto roomDto = appointmentService.getRoomByPatientOnDate(patientId, date.toString().trim());
        return roomDto != null ? ResponseEntity.ok(roomDto) : ResponseEntity.notFound().build();
    }
    @GetMapping("/patient/byappointmentid/{appointmentId}")
    public ResponseEntity<?> getPatientByAppointmentId(@PathVariable Integer appointmentId) {
        if (appointmentId == null || appointmentId <= 0) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "Invalid Request",
                            "message", "appointmentId must be a positive integer",
                            "appointmentId", appointmentId)
            );
        }

        Optional<PatientDto> patientOpt = appointmentService.getPatientByAppointmentId(appointmentId);
        return patientOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(
                        java.util.Map.of("error", "Not Found",
                                "message", "No patient found for appointment ID: " + appointmentId)
                ));


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String> > delete(@PathVariable Integer id) {
      String res=  appointmentService.deleteByAppointmentId(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", res);
        return ResponseEntity.ok(response);
    }




}


