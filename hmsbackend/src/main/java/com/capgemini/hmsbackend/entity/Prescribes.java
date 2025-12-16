
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescribes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class Prescribes {

    @EmbeddedId
    private PrescribesId id;

    @ManyToOne(optional = false)
    @MapsId("physicianId")
    @JoinColumn(name = "Physician")
    private Physician physician;

    @ManyToOne(optional = false)
    @MapsId("patientId")
    @JoinColumn(name = "Patient")
    private Patient patient;

    @ManyToOne(optional = false)
    @MapsId("medicationId")
    @JoinColumn(name = "Medication")
    private Medication medication;

    @ManyToOne
    @JoinColumn(name = "Appointment")
    private Appointment appointment;

    @Column(name = "Dose")
    private String dose;
}
