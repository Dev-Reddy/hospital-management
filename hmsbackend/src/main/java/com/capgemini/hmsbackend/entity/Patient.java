
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"appointments", "prescriptions", "stays", "undergoes"})
public class Patient {

    @Id
    @Column(name = "SSN")
    private Integer ssn;

    @Column(name = "Name")
    private String name;

    @Column(name = "Address")
    private String address;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "InsuranceID")
    private Integer insuranceId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PCP")
    private Physician pcp;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    private List<Prescribes> prescriptions;

    @OneToMany(mappedBy = "patient")
    private List<Stay> stays;

    @OneToMany(mappedBy = "patient")
    private List<Undergoes> undergoes;
}
