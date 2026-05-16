package com.hotel.RuSiri.DTO.Reservation;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class ReservationResponseDTO {
    private Long id;

    private String roomNumber;
    private String roomType;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private double totalPrice;

    private String status;
}
