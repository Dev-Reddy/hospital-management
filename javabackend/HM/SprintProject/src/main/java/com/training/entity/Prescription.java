package com.training.entity;

import jakarta.persistence.*;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Physician physician;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Medication medication;

    private String dosage;

    // getters and setters
}