
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class BlockId implements Serializable {

    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Column(name = "BlockCode")
    private Integer blockCode;
}
