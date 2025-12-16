
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "procedures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"trainings", "undergoes"})
public class Procedures {

    @Id
    @Column(name = "Code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;

    @Column(name = "Name")
    private String name;

    @Column(name = "Cost")
    private Double cost; // Consider BigDecimal for money

    @OneToMany(mappedBy = "procedure")
    private List<TrainedIn> trainings;

    @OneToMany(mappedBy = "procedure")
    private List<Undergoes> undergoes;
}
