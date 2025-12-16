package com.capgemini.hmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescribesDto {
    private Integer physicianId;
    private Integer patientId;
    private Integer medicationId;
    private LocalDateTime date;
    private Integer appointmentId; // optional
    private String dose;

    // new: human-friendly names
    private String physicianName;
    private String medicationName;
}
