package com.capgemini.hmsbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDtoBySarv {
    private Integer ssn;
    private String name;
    private String address;
    private String phone;
    private Integer insuranceId;
    /** Physician Primary Care Provider ID */
     private Integer pcpId;
}
