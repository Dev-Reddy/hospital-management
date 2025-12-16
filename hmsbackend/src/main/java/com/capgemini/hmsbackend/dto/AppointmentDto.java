
//package com.capgemini.hmsbackend.dto;
//
//import com.capgemini.hmsbackend.entity.*;
//import lombok.*;
//
//import java.util.List;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class AppointmentDto {
//
//    private Integer appointmentId;
//    private String appointmentStartDate;
//    private String appointmentEndDate;
//    private Integer patientId;
//    private String patientName;
//    private Integer nurseId;
//    private String nurseName;
//    private String examinantName;
//    private  Integer physicianId;
//    private String physicianName;
//
//
//
//}



package com.capgemini.hmsbackend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    private Integer appointmentId;
//private String appointmentStartDate;
//    private String appointmentEndDate;
    @NotNull(message = "Start date is required")
    private LocalDateTime starto;

    @NotNull(message = "End date is required")
    private LocalDateTime endo;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    private String patientName;

    private Integer nurseId;
    private String nurseName;

    @NotNull(message = "Physician ID is required")
    private Integer physicianId;
    private String examinantName;



//    private Integer roomNumber;      // from Room entity
//    private String roomType;         // optional

    private String physicianName;

    @NotBlank(message = "Examination room is required")
    private String examinationRoom;
}
