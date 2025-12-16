package com.capgemini.hmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurseDtoBySarv {
    private Integer employeeId;
    private String name;
    private String position;
    private boolean registered;
    private Integer ssn;
}
