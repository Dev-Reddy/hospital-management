package com.capgemini.hmsbackend.service;

import com.capgemini.hmsbackend.dto.RoomDto;
import com.capgemini.hmsbackend.entity.Block;
import com.capgemini.hmsbackend.entity.BlockId;
import com.capgemini.hmsbackend.entity.Room;
import com.capgemini.hmsbackend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl {
    private final RoomRepository roomRepo;
    public List<RoomDto> getAvailableRoomsByDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("date is required");
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd   = dayStart.plusDays(1);
        return roomRepo.findAvailableByDay(dayStart, dayEnd).stream()
                .map(this::roomEntityToDto)
                .toList();
    }

    public List<RoomDto> getAvailableRoomsByWindow(OffsetDateTime start, OffsetDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("End must be after start");
        }
        LocalDateTime s = start.toLocalDateTime();
        LocalDateTime e = end.toLocalDateTime();
        return roomRepo.findAvailableByWindow(s, e).stream()
                .map(this::roomEntityToDto)
                .toList();
    }


    public RoomDto roomEntityToDto(Room room) {

        Integer blockFloor = null;
        Integer blockCode = null;

        Block block = room.getBlock();
        if (block != null) {
            BlockId id = block.getId(); // Block uses embedded id
            if (id != null) {
                blockFloor = id.getBlockFloor();
                blockCode = id.getBlockCode();
            }
        }
            return RoomDto.builder()
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .blockFloor(room.getBlock() != null ? room.getBlock().getId().getBlockFloor(): null)
                .blockCode(room.getBlock() != null ? room.getBlock().getId().getBlockCode()  : null)
                .unavailable(room.isUnavailable())
                .build();
    }


}
