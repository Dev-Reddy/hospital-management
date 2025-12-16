
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "medication")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"prescriptions"})
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Code")
    private Integer code;

    @Column(name = "Name")
    private String name;

    @Column(name = "Brand")
    private String brand;

    

    @OneToMany(mappedBy = "medication")
    private List<Prescribes> prescriptions;
}
