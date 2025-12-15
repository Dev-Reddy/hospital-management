package com.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nurse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nurse {

    @Id
    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "name")
    private String name;

    @Column(name = "Position")
    private String position;

    @Column(name = "Registered")
    private Boolean registered;

    @Column(name = "SSN")
    private Integer ssn;

    @Column(name = "user_id")
    private Integer userId;

    // No FK in DB → keep as simple column
    @Column(name = "department_id")
    private Long departmentId;
}