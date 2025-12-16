package com.capgemini.hmsbackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PhysicianCreateDTO {
//    @NotBlank(message = "Employee id should not be blank")
//    private Integer employeeId;
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Position is mandatory")
    @Size(max = 100, message = "Position must be at most 100 characters")
    private String position;

    @NotNull(message = "SSN is mandatory")
    // You may change SSN to String if you need leading zeros.
    private Integer ssn;


}
