
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "affiliated_with")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class AffiliatedWith {

    @EmbeddedId
    private AffiliatedWithId id;

    @ManyToOne(optional = false)
    @MapsId("physicianId")
    @JoinColumn(name = "Physician")
    private Physician physician;

    @ManyToOne(optional = false)
    @MapsId("departmentId")
    @JoinColumn(name = "Department")
    private Department department;

    @Column(name = "PrimaryAffiliation")
    private boolean primaryAffiliation;
}
