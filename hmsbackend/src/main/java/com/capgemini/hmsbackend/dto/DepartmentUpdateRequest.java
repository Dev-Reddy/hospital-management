
package com.capgemini.hmsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentUpdateRequest {
    @NotBlank(message = "deptName must not be blank")
    @Size(max = 60, message = "deptName must be at most 60 characters")
    private String deptName;
}
