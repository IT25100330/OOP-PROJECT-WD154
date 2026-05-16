package com.hotel.RuSiri.DTO.Room;

import com.hotel.RuSiri.Entity.Room.AcType;
import com.hotel.RuSiri.Entity.Room.RoomStatus;
import com.hotel.RuSiri.Entity.Room.RoomType;
import com.hotel.RuSiri.Entity.Room.RoomView;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private RoomType type;
    private double price;
    private int capacity;
    private String description;
    private RoomStatus status;
    private RoomView view;
    private AcType acType;
}
