package com.hotel.RuSiri.DTO.Reservation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationSummaryDTO {

    private Long id;
    private String roomNumber;
    private String status;
    private double bookingFee;
}
