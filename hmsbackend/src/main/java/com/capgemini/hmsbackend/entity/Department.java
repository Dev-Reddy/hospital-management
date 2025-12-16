
package com.capgemini.hmsbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "department")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"affiliations"})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DepartmentID")
    private Integer departmentId;

    @Column(name = "Name")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Head")
    private Physician head;

    @OneToMany(mappedBy = "department")
    @JsonManagedReference
    private List<AffiliatedWith> affiliations;
}
