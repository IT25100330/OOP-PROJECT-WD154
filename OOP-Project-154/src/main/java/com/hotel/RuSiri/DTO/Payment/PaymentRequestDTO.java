package com.hotel.RuSiri.DTO.Payment;

import com.hotel.RuSiri.Entity.Payment.PaymentMethod;
import com.hotel.RuSiri.Entity.Payment.PaymentType;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long reservationId;
    private PaymentMethod method;
    private PaymentType type;
}
