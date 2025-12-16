package com.capgemini.hmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProceduresDto {
    private int code;
    private String name;
    private double cost;



}
