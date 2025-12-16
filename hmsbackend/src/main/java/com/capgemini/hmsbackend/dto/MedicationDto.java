package com.capgemini.hmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationDto {

    private Integer code; // medication code (primary key)
    private String name;
    private String brand;
    
}
