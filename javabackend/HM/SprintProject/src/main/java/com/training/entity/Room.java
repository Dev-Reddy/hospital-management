package com.training.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "RoomNumber")
    private Integer roomNumber;

    @Column(name = "Type")
    private String type; // adjust to real column if different

    @OneToMany(mappedBy = "room")
    private List<Stay> stays;

    // getters and setters
}