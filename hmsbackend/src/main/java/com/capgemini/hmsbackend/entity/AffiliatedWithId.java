
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class AffiliatedWithId implements Serializable {
    @Column(name = "Physician")
    private Integer physicianId;

    @Column(name = "Department")
    private Integer departmentId;
}
