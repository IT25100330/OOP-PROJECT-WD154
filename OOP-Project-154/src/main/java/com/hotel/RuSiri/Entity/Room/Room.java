package com.hotel.RuSiri.Entity.Room;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private double price;

    private int capacity;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Enumerated(EnumType.STRING)
    private RoomView view;

    @Enumerated(EnumType.STRING)
    private AcType acType;
}
