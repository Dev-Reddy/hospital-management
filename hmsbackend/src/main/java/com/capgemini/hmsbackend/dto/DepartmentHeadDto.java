package com.capgemini.hmsbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentHeadDto {
    @NotNull(message = "headId is required")
    @Positive(message = "headId must be positive")
    private Integer headId;
}

