package com.training.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "procedures")
public class Procedure {

    @Id
    @Column(name = "Code")
    private Integer code;

    @Column(name = "name")
    private String name;

    @Column(name = "Cost")
    private Double cost;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}