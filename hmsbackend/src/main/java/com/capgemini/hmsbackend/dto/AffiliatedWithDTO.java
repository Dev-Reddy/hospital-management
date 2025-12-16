package com.capgemini.hmsbackend.dto;

import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class AffiliatedWithDTO {
    private Integer physicianId;      // maps to Physician.employeeId
    private Integer departmentId;     // maps to Department.id
    private boolean primaryAffiliation;
}
