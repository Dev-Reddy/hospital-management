package com.capgemini.hmsbackend.dto;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    private Integer roomNumber;
    private String roomType;
    private Integer blockFloor;
    private Integer blockCode;
    private boolean unavailable;
}
