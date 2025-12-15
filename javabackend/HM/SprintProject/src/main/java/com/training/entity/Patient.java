package com.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @Column(name = "SSN")
    private Integer ssn;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "InsuranceID")
    private Integer insuranceId;

    @ManyToOne
    @JoinColumn(name = "PCP")
    private Physician primaryCarePhysician;

    @Column(name = "user_id")
    private Integer userId;
}
