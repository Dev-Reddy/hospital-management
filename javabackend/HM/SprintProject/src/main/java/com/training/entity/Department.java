package com.training.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @Column(name = "DepartmentID")
    private Integer departmentId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "Head")
    private Physician head;

    @Column(name = "dep_no")
    private Integer depNo;

    @Column(name = "dep_name")
    private String depName;

    @Column(name = "head_physician")
    private String headPhysicianName;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Physician getHead() {
        return head;
    }

    public void setHead(Physician head) {
        this.head = head;
    }

    public Integer getDepNo() {
        return depNo;
    }

    public void setDepNo(Integer depNo) {
        this.depNo = depNo;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getHeadPhysicianName() {
        return headPhysicianName;
    }

    public void setHeadPhysicianName(String headPhysicianName) {
        this.headPhysicianName = headPhysicianName;
    }
}
