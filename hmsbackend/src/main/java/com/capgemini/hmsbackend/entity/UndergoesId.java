
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class UndergoesId implements Serializable {

    @Column(name = "Patient")
    private Integer patientId;

    @Column(name = "Procedures")
    private Integer procedureId;

    @Column(name = "Stay")
    private Integer stayId;

    @Column(name = "DateUndergoes")
    private LocalDateTime dateUndergoes;
}
