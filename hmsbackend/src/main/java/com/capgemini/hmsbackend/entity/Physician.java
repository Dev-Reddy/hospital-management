
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "physician")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"departments", "appointments", "prescriptions", "proceduresPerformed", "patients"})
public class Physician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID" ,nullable=false)
    private Integer employeeId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Position")
    private String position;

    @Column(name = "SSN")
    private Integer ssn;

    // Reverse mappings
    @OneToMany(mappedBy = "head")
    private List<Department> departments;

    @OneToMany(mappedBy = "physician")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "physician")
    private List<Prescribes> prescriptions;

    @OneToMany(mappedBy = "physician")
    private List<Undergoes> proceduresPerformed;

    @OneToMany(mappedBy = "pcp")
    private List<Patient> patients;
}
