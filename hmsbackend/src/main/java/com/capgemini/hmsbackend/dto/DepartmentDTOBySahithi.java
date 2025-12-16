package com.capgemini.hmsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTOBySahithi {
    private Integer departmentId;
    private String name;
    private Integer headId;
    private String headName;
}
