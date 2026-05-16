package com.hotel.RuSiri.DTO.Room;

import com.hotel.RuSiri.Entity.Room.AcType;
import com.hotel.RuSiri.Entity.Room.RoomView;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableRoomDTO {
    private Long id;
    private String roomNumber;
    private String type;
    private double price;
    private RoomView view;
    private AcType acType;
}
