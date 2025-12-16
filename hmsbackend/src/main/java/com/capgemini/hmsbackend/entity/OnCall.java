
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "on_call")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @ManyToOne(optional = false)
    @MapsId("nurseId")
    @JoinColumn(name = "Nurse")
    private Nurse nurse;

    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor", insertable = false, updatable = false),
            @JoinColumn(name = "BlockCode", referencedColumnName = "BlockCode", insertable = false, updatable = false)
    })
    private Block block;
}
