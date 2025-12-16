
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stay")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"undergoes"})
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StayID")
    private Integer stayId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Patient")
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Room")
    private Room room;

    @Column(name = "StayStart")
    private LocalDateTime stayStart;

    @Column(name = "StayEnd")
    private LocalDateTime stayEnd;

    @OneToMany(mappedBy = "stay")
    private List<Undergoes> undergoes;
}
