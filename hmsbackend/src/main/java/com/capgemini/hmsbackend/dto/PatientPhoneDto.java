package com.capgemini.hmsbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientPhoneDto {
    @NotBlank(message = "phone is required")
    @Size(min = 6, max = 25, message = "phone must be between 6 and 25 characters")
    // Basic pattern allowing digits, spaces, +, -, parentheses
//    @Pattern(regexp = "^[0-9+()\\s-]+$", message = "phone contains invalid characters")
    private String phone;
}
