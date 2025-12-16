package com.capgemini.hmsbackend.dto;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDtoBySarv {

    private Integer appointmentId;
    private String appointmentStartDate;
    private String appointmentEndDate;
    private Integer patientId;          // instead of Patient entity
    private String patientName;         // optional for display
    private Integer nurseId;
    private String nurseName;
    private String examinantName;
    private  Integer physicianId;
    private String physicianName;


//    private Integer roomNumber;      // from Room entity
//    private String roomType;         // optional

    //   private List<String> prescriptionNames;

}