package com.capgemini.hmsbackend.dto;


import jakarta.validation.constraints.NotBlank;

import lombok.Data;
@Data

public class PatientUpdateRequest {
    @NotBlank(message = "Address must not be blank")
    private String address;
}
