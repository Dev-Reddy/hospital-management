
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trained_in")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class TrainedIn {

    @EmbeddedId
    private TrainedInId id;

    @ManyToOne(optional = false)
    @MapsId("physicianId")
    @JoinColumn(name = "Physician")
    private Physician physician;

    @ManyToOne(optional = false)
    @MapsId("treatmentId")
    @JoinColumn(name = "Treatment")
    private Procedures procedure;

    @Column(name = "CertificationDate")
    private LocalDateTime certificationDate;

    @Column(name = "CertificationExpires")
    private LocalDateTime certificationExpires;
}
