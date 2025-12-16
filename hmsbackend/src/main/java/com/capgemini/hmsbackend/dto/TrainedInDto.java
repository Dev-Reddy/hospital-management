package com.capgemini.hmsbackend.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainedInDto {
    private Integer physicianId;
    private Integer procedureCode;
    // use ISO-8601 strings in DTOs to avoid Jackson LocalDateTime parsing issues
    // Expected format: yyyy-MM-dd'T'HH:mm:ss (e.g. 2025-10-01T09:00:00)
    private String certificationDateIso;
    private String certificationExpiresIso;
}
