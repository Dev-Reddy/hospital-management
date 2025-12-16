
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "undergoes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class Undergoes {

    @EmbeddedId
    private UndergoesId id;

    @ManyToOne(optional = false)
    @MapsId("patientId")
    @JoinColumn(name = "Patient")
    private Patient patient;

    @ManyToOne(optional = false)
    @MapsId("procedureId")
    @JoinColumn(name = "Procedures")
    private Procedures procedure;

    @ManyToOne(optional = false)
    @MapsId("stayId")
    @JoinColumn(name = "Stay")
    private Stay stay;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Physician")
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "AssistingNurse")
    private Nurse assistingNurse;
}
