package com.capgemini.hmsbackend.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchItemDto {
    private Integer id;
    private String name;      // Single display field
    private String fullName;  // Mirror name for your frontend (optional)
}
