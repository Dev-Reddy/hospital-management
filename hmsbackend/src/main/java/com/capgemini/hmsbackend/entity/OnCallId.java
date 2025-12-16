
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class OnCallId implements Serializable {

    @Column(name = "Nurse")
    private Integer nurseId;

    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Column(name = "BlockCode")
    private Integer blockCode;

    @Column(name = "OnCallStart")
    private LocalDateTime onCallStart;

    @Column(name = "OnCallEnd")
    private LocalDateTime onCallEnd;
}