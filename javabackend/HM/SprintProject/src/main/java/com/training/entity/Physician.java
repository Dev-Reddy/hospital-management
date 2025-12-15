package com.training.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "physician")
public class Physician {

    @Id
    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "SSN")
    private Integer ssn;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "department_id")
    private Long departmentId;
    
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSsn() {
        return ssn;
    }

    public void setSsn(Integer ssn) {
        this.ssn = ssn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
