
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"rooms", "onCalls"})
public class Block {

    @EmbeddedId
    private BlockId id;

    @OneToMany(mappedBy = "block")
    private List<Room> rooms;

    @OneToMany(mappedBy = "block")
    private List<OnCall> onCalls;
}
