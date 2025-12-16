
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class PrescribesId implements Serializable {

    @Column(name = "Physician")
    private Integer physicianId;

    @Column(name = "Patient")
    private Integer patientId;

    @Column(name = "Medication")
    private Integer medicationId;

    @Column(name = "Date")
    private LocalDateTime date;
}
