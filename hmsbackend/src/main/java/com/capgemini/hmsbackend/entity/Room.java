
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "room")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"stays"})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomNumber")
    private Integer roomNumber;

    @Column(name = "RoomType")
    private String roomType;

    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor"),
            @JoinColumn(name = "BlockCode", referencedColumnName = "BlockCode")
    })
    private Block block;

    @Column(name = "Unavailable")
    private boolean unavailable;

    @OneToMany(mappedBy = "room")
    private List<Stay> stays;
}
