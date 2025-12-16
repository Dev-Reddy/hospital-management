
package com.capgemini.hmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NurseDTOBySahithi {
    private Integer employeeId;
    private String name;
    private String position;
    private boolean registered;
    private  Integer ssn;
}
