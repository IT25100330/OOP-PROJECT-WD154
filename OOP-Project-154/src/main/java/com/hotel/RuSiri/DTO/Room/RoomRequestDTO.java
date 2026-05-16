package com.hotel.RuSiri.DTO.Room;

import com.hotel.RuSiri.Entity.Room.AcType;
import com.hotel.RuSiri.Entity.Room.RoomStatus;
import com.hotel.RuSiri.Entity.Room.RoomType;
import com.hotel.RuSiri.Entity.Room.RoomView;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomRequestDTO {

    @NotBlank
    @Column(unique = true)

    private String roomNumber;
    private RoomType type;

    @Min(1)
    private double price;

    @Min(1)
    private int capacity;

    private String description;
    private RoomStatus status;
    private RoomView view;
    private AcType acType;

}
