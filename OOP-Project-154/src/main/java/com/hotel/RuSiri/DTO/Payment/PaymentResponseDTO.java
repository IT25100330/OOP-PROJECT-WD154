package com.hotel.RuSiri.DTO.Payment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {
    private Long id;

    private Long reservationId;
    private String roomNumber;

    private double amount;
    private String paymentMethod;
    private String status;

    private LocalDateTime paymentDate;
}
