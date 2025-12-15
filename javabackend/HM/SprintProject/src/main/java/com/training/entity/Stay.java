package com.training.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "stay")
public class Stay {

    @Id
    @Column(name = "StayID")
    private Integer stayId;

    @ManyToOne
    @JoinColumn(name = "Patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "Room")
    private Room room;

    @Column(name = "StayStart")
    private LocalDateTime stayStart;

    @Column(name = "StayEnd")
    private LocalDateTime stayEnd;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // getters and setters
}