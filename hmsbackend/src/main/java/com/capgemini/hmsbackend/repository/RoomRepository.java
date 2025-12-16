package com.capgemini.hmsbackend.repository;


import com.capgemini.hmsbackend.entity.Room;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    /** Available rooms in a specific time window: no overlapping stay AND not unavailable */
    @Query("""
        SELECT r FROM Room r
        WHERE (r.unavailable IS NULL OR r.unavailable = false)
          AND NOT EXISTS (
              SELECT 1 FROM Stay s
              WHERE s.room.roomNumber = r.roomNumber
                AND s.stayStart < :end
                AND s.stayEnd   > :start
          )
        ORDER BY r.roomNumber
    """)
    List<Room> findAvailableByWindow(@Param("start") LocalDateTime start,
                                     @Param("end")   LocalDateTime end);

    /** Available rooms for a whole day [dayStart, dayEnd) */
    @Query("""
        SELECT r FROM Room r
        WHERE (r.unavailable IS NULL OR r.unavailable = false)
          AND NOT EXISTS (
              SELECT 1 FROM Stay s
              WHERE s.room.roomNumber = r.roomNumber
                AND s.stayStart < :dayEnd
                AND s.stayEnd   > :dayStart
          )
        ORDER BY r.roomNumber
    """)
    List<Room> findAvailableByDay(@Param("dayStart") LocalDateTime dayStart,
                                  @Param("dayEnd")   LocalDateTime dayEnd);
}
