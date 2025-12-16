package com.capgemini.hmsbackend.controller;
import com.capgemini.hmsbackend.dto.RoomDto;
import com.capgemini.hmsbackend.service.RoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@CrossOrigin // or configure globally in WebConfig
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomsController {

    private final RoomServiceImpl service;

    @GetMapping("/available/{starto}/{endo}")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByWindow(
            @RequestParam String starto,
            @RequestParam String endo
    ) {
        return ResponseEntity.ok(
                service.getAvailableRoomsByWindow(OffsetDateTime.parse(starto), OffsetDateTime.parse(endo))
        );
    }

    @GetMapping("/available/{date}")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(service.getAvailableRoomsByDate(date));
    }
}
