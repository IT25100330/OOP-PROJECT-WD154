package com.hotel.RuSiri.DTO.Reservation;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {

    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
