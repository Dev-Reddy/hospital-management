package com.capgemini.hmsbackend.dto;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProceduresCreateDto {

//    @NotNull(message = "Code is required")
//    private Integer code;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 30)
    private String name;

    @NotNull(message = "Cost is required")
    private Double cost;
}
