
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "nurse")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"appointments", "assists", "onCalls"})
public class Nurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Position")
    private String position;

    @Column(name = "Registered")
    private boolean registered;

    @Column(name = "SSN")
    private Integer ssn;


    @OneToMany(mappedBy = "prepNurse")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "assistingNurse")
    private List<Undergoes> assists;

    @OneToMany(mappedBy = "nurse")
    private List<OnCall> onCalls;

}